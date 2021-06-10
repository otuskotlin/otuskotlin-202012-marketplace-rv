package ru.otus.otuskotlin.marketplace.backend.repository.sql

import jdk.nashorn.internal.runtime.Debug.id
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.otus.otuskotlin.marketplace.backend.repository.sql.schemas.tags.TagDto
import ru.otus.otuskotlin.marketplace.backend.repository.sql.schemas.tags.TagsTable
import ru.otus.otuskotlin.marketplace.backend.repository.sql.schemas.workshops.WorkshopDto
import ru.otus.otuskotlin.marketplace.backend.repository.sql.schemas.workshops.WorkshopsTable
import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.common.backend.exceptions.MpRepoWrongIdException
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtIdModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpWorkshopIdModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpWorkshopModel
import ru.otus.otuskotlin.marketplace.common.backend.repositories.IWorkshopRepository
import java.sql.Connection
import java.util.*

class WorkshopRepoSql(
    url: String = "jdbc:postgresql://localhost:5432/marketplace",
    driver: String = "org.postgresql.Driver",
    user: String = "postgres",
    password: String = "postgres",
    val printLogs: Boolean = false,
    val adTagCompanion: UUIDEntityClass<TagDto>,

    initObjects: Collection<MpWorkshopModel> = emptyList()
) : IWorkshopRepository {

    private val db by lazy {
        val _db = Database.connect(
            url = url,
            driver = driver,
            user = user,
            password = password
        )
        transaction {
            SchemaUtils.create(WorkshopsTable)
        }
        _db
    }

    init {
        runBlocking {
            initObjects.forEach {
                createWithId(MpBeContext(requestWorkshop = it), true)
            }
        }
    }

    override suspend fun create(context: MpBeContext): MpWorkshopModel = createWithId(context)

    private suspend fun createWithId(context: MpBeContext, setId: Boolean = false): MpWorkshopModel {
        val model = context.requestWorkshop
        return transaction(db) {
            if (printLogs) addLogger(StdOutSqlLogger)
            val workshopNew = WorkshopDto.new(if (setId) model.id.asUUID() else null)
                 { of(model)}
            val WorkshopNewId = workshopNew.id
            val WorkshopTags = workshopNew.tagIds //TODO Тэги??
            context.responseWorkshop = WorkshopDto[WorkshopNewId].toModel()
            context.responseWorkshop
        }
    }

    override suspend fun read(context: MpBeContext): MpWorkshopModel {
        val id = context.requestWorkshopId
        if (id == MpWorkshopIdModel.NONE) throw MpRepoWrongIdException(id.id)
        return transaction {
            if (printLogs) addLogger(StdOutSqlLogger)
            context.responseWorkshop = WorkshopDto[id.asUUID()].toModel()
            context.responseWorkshop
        }
    }

    override suspend fun update(context: MpBeContext): MpWorkshopModel {
        if (context.requestWorkshop.id == MpWorkshopIdModel.NONE)
            throw MpRepoWrongIdException(context.requestWorkshop.id.id)
        val model = context.requestWorkshop
        val eventId = model.id.asUUID()
        return transaction(db) {
            if (printLogs) addLogger(StdOutSqlLogger)
            val workshopToUpdate = WorkshopDto[eventId]
            workshopToUpdate
                .apply { of(model) }
                .toModel()
            context.responseWorkshop = WorkshopDto[eventId].toModel()
            context.responseWorkshop
        }
    }

    override suspend fun delete(context: MpBeContext): MpWorkshopModel {
        if (context.requestWorkshop.id == MpWorkshopIdModel.NONE)
            throw MpRepoWrongIdException(context.requestWorkshop.id.id)
        val model = context.requestWorkshop
        val eventId = model.id.asUUID()
        return transaction(db) {
            if (printLogs) addLogger(StdOutSqlLogger)
            val workshopToUpdate = WorkshopDto[eventId]
            workshopToUpdate
                .apply { of(model) }
                .toModel()
            context.responseWorkshop = WorkshopDto[eventId].toModel()
            context.responseWorkshop
        }
    }

    override suspend fun list(context: MpBeContext): Collection<MpWorkshopModel> {
        val filter = context.requestWorkshopFilter
        return transaction(
            transactionIsolation = Connection.TRANSACTION_SERIALIZABLE,
            repetitionAttempts = 3,
            db = db
        ) {
            if (printLogs) addLogger(StdOutSqlLogger)
            val found =
                if (filter.text.isNotBlank()) {
                    WorkshopDto.find {
                        (WorkshopsTable.title like "%${filter.text}%") or (WorkshopsTable.description like "%${filter.text}%")
                    }
                } else {
                    WorkshopDto.all()
                }
            context.pageCount = found.count().toInt()
            found
                .limit(filter.count.takeIf { it > 0 } ?: 20, filter.offset.toLong().takeIf { it > 0 } ?: 0)
                .map { it.toModel() }.toList()
            context.responseWorkshops
        }
    }
}