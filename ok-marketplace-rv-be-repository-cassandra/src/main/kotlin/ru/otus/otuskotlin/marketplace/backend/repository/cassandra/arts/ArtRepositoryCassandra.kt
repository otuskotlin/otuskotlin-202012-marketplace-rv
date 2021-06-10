package ru.otus.otuskotlin.marketplace.backend.repository.cassandra.arts

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
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtIdModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtModel
import ru.otus.otuskotlin.marketplace.common.backend.repositories.IArtRepository
import java.time.Duration
import java.util.*

class ArtRepositoryCassandra(
    override val keyspaceName: String,
    override val hosts: String = "",
    override val port: Int = 9042,
    override val user: String = "cassandra",
    override val pass: String = "cassandra",
    override val replicationFactor: Int = 1,
    override val testing: Boolean = false,
    private val timeout: Duration = Duration.ofSeconds(30),
    initObjects: Collection<MpArtModel> = emptyList(),
): IArtRepository, MpRepositoryCassandra(keyspaceName, hosts, port, user, pass, replicationFactor) {

    private val mapper by lazy {
        ArtCassandraMapperBuilder(session).build()
    }

    /**
     *  DAO для операций по id
     */
    private val daoById by lazy {
        mapper.artByIdDao(keyspaceName, ArtByIdCassandraDto.ARTS_TABLE_NAME).apply {
            runBlocking {
                initObjects.map { model ->
                    withTimeout(timeout.toMillis()) {
                        createAsync(ArtByIdCassandraDto.of(model)).await()
                    }
                }
            }
        }
    }

    /**
     *  DAO для операций с названиями
     */
    private val daoByTitle by lazy {
        mapper.artByTitleDao(keyspaceName, ArtByTitleCassandraDto.ARTS_TITLE_TABLE_NAME).apply {
            runBlocking {
                initObjects.map { model ->
                    withTimeout(timeout.toMillis()) {
                        createAsync(ArtByTitleCassandraDto.of(model)).await()
                    }
                }
            }
        }
    }

    override suspend fun read(context: MpBeContext): MpArtModel {
        val id = context.requestArtId
        if (id == MpArtIdModel.NONE) throw MpRepoWrongIdException(id.id)
        return withTimeout(timeout.toMillis()) {
            val model = daoById.readAsync(id.id).await()?.toModel() ?: throw MpRepoNotFoundException(id.id)
            context.responseArt = model
            model
        }
    }

    /**
     * Запись происходит во все таблицы
     */
    override suspend fun create(context: MpBeContext): MpArtModel {
        val id = UUID.randomUUID().toString()
        val dtoById = ArtByIdCassandraDto.of(context.requestArt, id)
        val dtoByTitle = ArtByTitleCassandraDto.of(context.requestArt, id)
        return withTimeout(timeout.toMillis()) {
            daoById.createAsync(dtoById).await()
            daoByTitle.createAsync(dtoByTitle).await()
            val model = daoById.readAsync(id).await()?.toModel()?: throw MpRepoNotFoundException(id)
            context.responseArt = model
            model
        }
    }

    /**
     *  Использование Optimistic Lock для примера, в данном случае update аналогичен create
     */
    override suspend fun update(context: MpBeContext): MpArtModel {
        val id = context.requestArt.id
        if (id == MpArtIdModel.NONE) throw MpRepoWrongIdException(id.id)
        return withTimeout(timeout.toMillis()) {
            val lockKey = daoById.readAsync(id.id).await()?.lockVersion ?: throw MpRepoNotFoundException(id.id)
            val dtoById = ArtByIdCassandraDto.of(context.requestArt)
            val dtoByTitle = ArtByTitleCassandraDto.of(context.requestArt)
            val isUpdated = daoById.updateAsync(dtoById, lockKey).await()
            if (!isUpdated) throw MpRepoModifyException(id.id)
            daoByTitle.createAsync(dtoByTitle).await()
            val model = daoById.readAsync(id.id).await()?.toModel() ?: throw MpRepoNotFoundException(id.id)
            context.responseArt = model
            model
        }
    }

    /**
     * Удаление записи из всех таблиц
     */
    override suspend fun delete(context: MpBeContext): MpArtModel {
        val id = context.requestArtId
        if (id == MpArtIdModel.NONE) throw MpRepoWrongIdException(id.id)
        return withTimeout(timeout.toMillis()) {
            val model = daoById.readAsync(id.id).await()?.toModel() ?: throw MpRepoNotFoundException(id.id)
            daoById.deleteAsync(id.id).await()
            daoByTitle.deleteAsync(ArtByTitleCassandraDto.of(model)).await()
            context.responseArt = model
            model
        }
    }

    override suspend fun list(context: MpBeContext): Collection<MpArtModel> {
        val filter = context.requestArtFilter
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
             context.responseArts = list.toMutableList()
             context.pageCount = list.count().takeIf { it != 0 }
                 ?.let { (recordsCount.toDouble() / it + 0.5).toInt() }
                 ?: Int.MIN_VALUE
             list
        }
    }

    override fun CqlSession.createTables() {
        execute(
            SchemaBuilder.createTable(ArtByTitleCassandraDto.ARTS_TITLE_TABLE_NAME)
                .ifNotExists()
                .withPartitionKey(ArtByTitleCassandraDto.TITLE, DataTypes.TEXT)
                .withClusteringColumn(ArtByTitleCassandraDto.TIMESTAMP, DataTypes.TIMESTAMP)
                .withClusteringColumn(ArtByTitleCassandraDto.ID, DataTypes.TEXT)
                .withClusteringColumn(ArtByTitleCassandraDto.TITLE_INDEX, DataTypes.TEXT)
                .withClusteringOrder(ArtByTitleCassandraDto.TIMESTAMP, ClusteringOrder.DESC)
                .withClusteringOrder(ArtByTitleCassandraDto.ID, ClusteringOrder.ASC)
                .withClusteringOrder(ArtByTitleCassandraDto.TITLE_INDEX, ClusteringOrder.ASC)
                .build()
        )
        execute(
            SchemaBuilder.createTable(ArtByIdCassandraDto.ARTS_TABLE_NAME)
                .ifNotExists()
                .withPartitionKey(ArtByIdCassandraDto.ID, DataTypes.TEXT)
                .withColumn(ArtByIdCassandraDto.DESCRIPTION, DataTypes.TEXT)
                .withColumn(ArtByIdCassandraDto.TITLE, DataTypes.TEXT)
                .withColumn(ArtByIdCassandraDto.TAG_ID_LIST, DataTypes.setOf(DataTypes.TEXT))
                .withColumn(ArtByIdCassandraDto.LOCK_VERSION, DataTypes.TEXT)
                .build()
        )
    }

    override fun CqlSession.createIndexes() {
        execute(
            SchemaBuilder.createIndex()
                .ifNotExists()
                .usingSASI()
                .onTable(ArtByTitleCassandraDto.ARTS_TITLE_TABLE_NAME)
                .andColumn(ArtByTitleCassandraDto.TITLE_INDEX)
                .withSASIOptions(mapOf("mode" to "CONTAINS", "tokenization_locale" to "en"))
                .build()
        )
    }

    override fun init() = apply {
        val dao1 = daoById
        val dao2 = daoByTitle
    }

}
