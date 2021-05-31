package ru.otus.otuskotlin.marketplace.backend.repository.sql

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.otus.otuskotlin.marketplace.backend.repository.sql.schemas.arts.ArtDto
import ru.otus.otuskotlin.marketplace.backend.repository.sql.schemas.arts.ArtsTable
import ru.otus.otuskotlin.marketplace.backend.repository.sql.schemas.tags.TagDto
import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.common.backend.exceptions.MpRepoWrongIdException
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtIdModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtModel
import ru.otus.otuskotlin.marketplace.common.backend.repositories.IArtRepository
import java.sql.Connection

class ArtRepoSql(
    url: String = "jdbc:postgresql://localhost:5432/marketplace",
    driver: String = "org.postgresql.Driver",
    user: String = "postgres",
    password: String = "postgres",
    val printLogs: Boolean = false,
    val adTagCompanion: UUIDEntityClass<TagDto>,

    initObjects: Collection<MpArtModel> = emptyList()
) : IArtRepository {

    private val db by lazy {
        val _db = Database.connect(
            url = url,
            driver = driver,
            user = user,
            password = password
        )
        transaction {
            SchemaUtils.create(ArtsTable)
        }
        _db
    }

    init {
        runBlocking {
            initObjects.forEach {
                createWithId(MpBeContext(requestArt = it), true)
            }
        }
    }

    override suspend fun create(context: MpBeContext): MpArtModel = createWithId(context)

    private suspend fun createWithId(context: MpBeContext, setId: Boolean = false): MpArtModel {
        val model = context.requestArt
        return transaction(db) {
            if (printLogs) addLogger(StdOutSqlLogger)
            val artNew = ArtDto.new(if (setId) model.id.asUUID() else null)
                 { of(model)}
            val ArtNewId = artNew.id
            val ArtTags = artNew.tagIds //TODO Тэги??
            context.responseArt = ArtDto[ArtNewId].toModel()
            context.responseArt
        }
    }

    override suspend fun read(context: MpBeContext): MpArtModel {
        val id = context.requestArtId
        if (id == MpArtIdModel.NONE) throw MpRepoWrongIdException(id.id)
        return transaction {
            if (printLogs) addLogger(StdOutSqlLogger)
            context.responseArt = ArtDto[id.asUUID()].toModel()
            context.responseArt
        }
    }

    override suspend fun update(context: MpBeContext): MpArtModel {
        if (context.requestArt.id == MpArtIdModel.NONE)
            throw MpRepoWrongIdException(context.requestArt.id.id)
        val model = context.requestArt
        val eventId = model.id.asUUID()
        return transaction(db) {
            if (printLogs) addLogger(StdOutSqlLogger)
            val artToUpdate = ArtDto[eventId]
            artToUpdate
                .apply { of(model) }
                .toModel()
            context.responseArt = ArtDto[eventId].toModel()
            context.responseArt
        }
    }

    override suspend fun delete(context: MpBeContext): MpArtModel {
        if (context.requestArt.id == MpArtIdModel.NONE)
            throw MpRepoWrongIdException(context.requestArt.id.id)
        val model = context.requestArt
        val eventId = model.id.asUUID()
        return transaction(db) {
            if (printLogs) addLogger(StdOutSqlLogger)
            val artToUpdate = ArtDto[eventId]
            artToUpdate
                .apply { of(model) }
                .toModel()
            context.responseArt = ArtDto[eventId].toModel()
            context.responseArt
        }
    }

    override suspend fun list(context: MpBeContext): Collection<MpArtModel> {
        val filter = context.requestArtFilter
        return transaction(
            transactionIsolation = Connection.TRANSACTION_SERIALIZABLE,
            repetitionAttempts = 3,
            db = db
        ) {
            if (printLogs) addLogger(StdOutSqlLogger)
            val found =
                if (filter.text.isNotBlank()) {
                    ArtDto.find {
                        (ArtsTable.title like "%${filter.text}%") or (ArtsTable.description like "%${filter.text}%")
                    }
                } else {
                    ArtDto.all()
                }
            context.pageCount = found.count().toInt()
            found
                .limit(filter.count.takeIf { it > 0 } ?: 20, filter.offset.toLong().takeIf { it > 0 } ?: 0)
                .map { it.toModel() }.toList()
            context.responseArts
        }
    }
}