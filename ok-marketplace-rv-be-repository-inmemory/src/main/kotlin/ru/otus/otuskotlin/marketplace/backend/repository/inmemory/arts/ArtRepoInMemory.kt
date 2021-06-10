package ru.otus.otuskotlin.marketplace.backend.repository.inmemory.arts

import org.cache2k.Cache
import org.cache2k.Cache2kBuilder
import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.common.backend.exceptions.MpRepoIndexException
import ru.otus.otuskotlin.marketplace.common.backend.exceptions.MpRepoNotFoundException
import ru.otus.otuskotlin.marketplace.common.backend.exceptions.MpRepoWrongIdException
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtIdModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtModel
import ru.otus.otuskotlin.marketplace.common.backend.repositories.IArtRepository
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class ArtRepoInMemory @OptIn(ExperimentalTime::class) constructor(
    ttl: Duration,
    initObjects: Collection<MpArtModel> = emptyList()
): IArtRepository {
    @OptIn(ExperimentalTime::class)
    private var cache: Cache<String, ArtInMemoryDto> = object : Cache2kBuilder<String, ArtInMemoryDto>() {}
        .expireAfterWrite(ttl.toLongMilliseconds(), TimeUnit.MILLISECONDS) // expire/refresh after 5 minutes
        .suppressExceptions(false)
        .build()
        .also { cache ->
            initObjects.forEach {
                cache.put(it.id.id, ArtInMemoryDto.of(it))
            }
        }

    override suspend fun create(context: MpBeContext): MpArtModel {
        val dto = ArtInMemoryDto.of(context.requestArt, UUID.randomUUID().toString())
        val model = save(dto).toModel()
        context.responseArt = model
        return model
    }

    override suspend fun read(context: MpBeContext): MpArtModel {
        val id = context.requestArtId
        if (id == MpArtIdModel.NONE) throw MpRepoWrongIdException(id.id)
        val model = cache.get(id.id)?.toModel()?: throw MpRepoNotFoundException(id.id)
        context.responseArt = model
        return model
    }


    override suspend fun update(context: MpBeContext): MpArtModel {
        if (context.requestArt.id == MpArtIdModel.NONE) throw MpRepoWrongIdException(context.requestArt.id.id)
        val model = save(ArtInMemoryDto.of(context.requestArt)).toModel()
        context.responseArt = model
        return model
    }

    override suspend fun delete(context: MpBeContext): MpArtModel {
        val id = context.requestArtId
        if (id == MpArtIdModel.NONE) throw MpRepoWrongIdException(id.id)
        val model = cache.peekAndRemove(id.id)?.toModel()?: throw MpRepoNotFoundException(id.id)
        context.responseArt = model
        return model
    }

    override suspend fun list(context: MpBeContext): Collection<MpArtModel> {
        val textFilter = context.requestArtFilter.text
        if (textFilter.length < 3) throw MpRepoIndexException(textFilter)
        val records = cache.asMap().filterValues {
            it.title?.contains(textFilter, true)?:false || if (context.requestArtFilter.includeDescription) {
                it.description?.contains(textFilter, true) ?: false
            } else false
        }.values
        if (records.count() <= context.requestArtFilter.offset)
            throw MpRepoIndexException(textFilter)
        val list = records.toList()
            .subList(
                context.requestArtFilter.offset,
                if (records.count() >= context.requestArtFilter.offset + context.requestArtFilter.count)
                    context.requestArtFilter.offset + context.requestArtFilter.count
                else records.count()
            ).map { it.toModel() }
        context.responseArts = list.toMutableList()
        context.pageCount = list.count().takeIf { it != 0 }
            ?.let { (records.count().toDouble() / it + 0.5).toInt() }
            ?: Int.MIN_VALUE
        return list
    }

    private suspend fun save(dto: ArtInMemoryDto): ArtInMemoryDto {
        cache.put(dto.id, dto)
        return cache.get(dto.id)
    }
}