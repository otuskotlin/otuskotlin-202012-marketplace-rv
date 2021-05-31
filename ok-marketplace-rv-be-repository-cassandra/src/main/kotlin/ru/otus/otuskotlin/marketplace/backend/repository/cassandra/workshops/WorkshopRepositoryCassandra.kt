package ru.otus.otuskotlin.marketplace.backend.repository.cassandra.workshops

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder
import com.datastax.oss.driver.api.core.type.DataTypes
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.guava.await
import ru.otus.otuskotlin.marketplace.backend.repository.cassandra.common.MpRepositoryCassandra
import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.common.backend.exceptions.MpRepoIndexException
import ru.otus.otuskotlin.marketplace.common.backend.exceptions.MpRepoModifyException
import ru.otus.otuskotlin.marketplace.common.backend.exceptions.MpRepoNotFoundException
import ru.otus.otuskotlin.marketplace.common.backend.exceptions.MpRepoWrongIdException
import ru.otus.otuskotlin.marketplace.common.backend.models.MpWorkshopIdModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpWorkshopModel
import ru.otus.otuskotlin.marketplace.common.backend.repositories.IWorkshopRepository
import java.time.Duration
import java.util.*

class WorkshopRepositoryCassandra(
    override val keyspaceName: String,
    override val hosts: String = "",
    override val port: Int = 9042,
    override val user: String = "cassandra",
    override val pass: String = "cassandra",
    override val replicationFactor: Int = 1,
    override val testing: Boolean = false,
    private val timeout: Duration = Duration.ofSeconds(30),
    initObjects: Collection<MpWorkshopModel> = emptyList(),
) : IWorkshopRepository, MpRepositoryCassandra(keyspaceName, hosts, port, user, pass, replicationFactor) {

    private val mapper by lazy {
        WorkshopCassandraMapperBuilder(session).build()
    }

    /**
     *  DAO для операций по id
     */
    private val daoById by lazy {
        mapper.workshopByIdDao(keyspaceName, WorkshopByIdCassandraDto.WORKSHOPS_TABLE_NAME).apply {
            runBlocking {
                initObjects.map { model ->
                    withTimeout(timeout.toMillis()) {
                        createAsync(WorkshopByIdCassandraDto.of(model)).await()
                    }
                }
            }
        }
    }

    /**
     *  DAO для операций с названиями
     */
    private val daoByTitle by lazy {
        mapper.workshopByTitleDao(keyspaceName, WorkshopByTitleCassandraDto.WORKSHOPS_TITLE_TABLE_NAME).apply {
            runBlocking {
                initObjects.map { model ->
                    withTimeout(timeout.toMillis()) {
                        createAsync(WorkshopByTitleCassandraDto.of(model)).await()
                    }
                }
            }
        }
    }

    override suspend fun read(context: MpBeContext): MpWorkshopModel {
        val id = context.requestWorkshopId
        if (id == MpWorkshopIdModel.NONE) throw MpRepoWrongIdException(id.id)
        return withTimeout(timeout.toMillis()) {
            val model = daoById.readAsync(id.id).await()?.toModel() ?: throw MpRepoNotFoundException(id.id)
            context.responseWorkshop = model
            model
        }
    }

    /**
     * Запись происходит во все таблицы
     */
    override suspend fun create(context: MpBeContext): MpWorkshopModel {
        val id = UUID.randomUUID().toString()
        val dtoById = WorkshopByIdCassandraDto.of(context.requestWorkshop, id)
        val dtoByTitle = WorkshopByTitleCassandraDto.of(context.requestWorkshop, id)
        return withTimeout(timeout.toMillis()) {
            daoById.createAsync(dtoById).await()
            daoByTitle.createAsync(dtoByTitle).await()
            val model = daoById.readAsync(id).await()?.toModel() ?: throw MpRepoNotFoundException(id)
            context.responseWorkshop = model
            model
        }
    }

    /**
     *  Использование Optimistic Lock для примера, в данном случае update аналогичен create
     */
    override suspend fun update(context: MpBeContext): MpWorkshopModel {
        val id = context.requestWorkshop.id
        if (id == MpWorkshopIdModel.NONE) throw MpRepoWrongIdException(id.id)
        return withTimeout(timeout.toMillis()) {
            val lockKey = daoById.readAsync(id.id).await()?.lockVersion ?: throw MpRepoNotFoundException(id.id)
            val dtoById = WorkshopByIdCassandraDto.of(context.requestWorkshop)
            val dtoByTitle = WorkshopByTitleCassandraDto.of(context.requestWorkshop)
            val isUpdated = daoById.updateAsync(dtoById, lockKey).await()
            if (!isUpdated) throw MpRepoModifyException(id.id)
            daoByTitle.createAsync(dtoByTitle).await()
            val model = daoById.readAsync(id.id).await()?.toModel() ?: throw MpRepoNotFoundException(id.id)
            context.responseWorkshop = model
            model
        }
    }

    /**
     * Удаление записи из всех таблиц
     */
    override suspend fun delete(context: MpBeContext): MpWorkshopModel {
        val id = context.requestWorkshopId
        if (id == MpWorkshopIdModel.NONE) throw MpRepoWrongIdException(id.id)
        return withTimeout(timeout.toMillis()) {
            val model = daoById.readAsync(id.id).await()?.toModel() ?: throw MpRepoNotFoundException(id.id)
            daoById.deleteAsync(id.id).await()
            daoByTitle.deleteAsync(WorkshopByTitleCassandraDto.of(model)).await()
            context.responseWorkshop = model
            model
        }
    }

    override suspend fun list(context: MpBeContext): Collection<MpWorkshopModel> {
        val filter = context.requestWorkshopFilter
        var lastIndex = filter.offset + filter.count
        if (filter.text.length < 3) throw MpRepoIndexException(filter.text)
        return withTimeout(timeout.toMillis()) {
            val records = daoByTitle.filterByTitleAsync("%${filter.text}%").await().toList()
                .sortedByDescending { it.timestamp }
            val recordsCount = records.count()
            if (recordsCount < lastIndex) lastIndex = recordsCount
            val list = flow {
                for (pos in filter.offset until lastIndex) {
                    records[pos].id?.let { id ->
                        emit(daoById.readAsync(id).await()?.toModel() ?: throw MpRepoNotFoundException(id))
                    }
                }
            }.toList()
            context.responseWorkshops = list.toMutableList()
            context.pageCount = list.count().takeIf { it != 0 }
                ?.let { (recordsCount.toDouble() / it + 0.5).toInt() }
                ?: Int.MIN_VALUE
            list
        }

    }

    override fun CqlSession.createTables() {
        execute(
            SchemaBuilder.createTable(WorkshopByTitleCassandraDto.WORKSHOPS_TITLE_TABLE_NAME)
                .ifNotExists()
                .withPartitionKey(WorkshopByTitleCassandraDto.TITLE, DataTypes.TEXT)
                .withClusteringColumn(WorkshopByTitleCassandraDto.TIMESTAMP, DataTypes.TIMESTAMP)
                .withClusteringColumn(WorkshopByTitleCassandraDto.ID, DataTypes.TEXT)
                .withClusteringColumn(WorkshopByTitleCassandraDto.TITLE_INDEX, DataTypes.TEXT)
                .withClusteringOrder(WorkshopByTitleCassandraDto.TIMESTAMP, ClusteringOrder.DESC)
                .withClusteringOrder(WorkshopByTitleCassandraDto.ID, ClusteringOrder.ASC)
                .withClusteringOrder(WorkshopByTitleCassandraDto.TITLE_INDEX, ClusteringOrder.ASC)
                .build()
        )
        execute(
            SchemaBuilder.createTable(WorkshopByIdCassandraDto.WORKSHOPS_TABLE_NAME)
                .ifNotExists()
                .withPartitionKey(WorkshopByIdCassandraDto.ID, DataTypes.TEXT)
                .withColumn(WorkshopByIdCassandraDto.DESCRIPTION, DataTypes.TEXT)
                .withColumn(WorkshopByIdCassandraDto.TITLE, DataTypes.TEXT)
                .withColumn(WorkshopByIdCassandraDto.TAG_ID_LIST, DataTypes.setOf(DataTypes.TEXT))
                .withColumn(WorkshopByIdCassandraDto.LOCK_VERSION, DataTypes.TEXT)
                .build()
        )
    }

    override fun CqlSession.createIndexes() {
        execute(
            SchemaBuilder.createIndex()
                .ifNotExists()
                .usingSASI()
                .onTable(WorkshopByTitleCassandraDto.WORKSHOPS_TITLE_TABLE_NAME)
                .andColumn(WorkshopByTitleCassandraDto.TITLE_INDEX)
                .withSASIOptions(mapOf("mode" to "CONTAINS", "tokenization_locale" to "en"))
                .build()
        )
    }

    override fun init() = apply {
        val dao1 = daoById
        val dao2 = daoByTitle
    }

}
