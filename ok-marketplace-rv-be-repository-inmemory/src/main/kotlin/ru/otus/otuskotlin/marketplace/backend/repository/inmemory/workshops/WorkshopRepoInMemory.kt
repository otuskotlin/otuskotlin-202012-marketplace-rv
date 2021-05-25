package ru.otus.otuskotlin.marketplace.backend.repository.inmemory.workshops

import org.cache2k.Cache
import org.cache2k.Cache2kBuilder
import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.common.backend.exceptions.MpRepoIndexException
import ru.otus.otuskotlin.marketplace.common.backend.exceptions.MpRepoNotFoundException
import ru.otus.otuskotlin.marketplace.common.backend.exceptions.MpRepoWrongIdException
import ru.otus.otuskotlin.marketplace.common.backend.models.MpWorkshopIdModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpWorkshopModel
import ru.otus.otuskotlin.marketplace.common.backend.repositories.IWorkshopRepository
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class WorkshopRepoInMemory @OptIn(ExperimentalTime::class) constructor(
    ttl: Duration,
    initObjects: Collection<MpWorkshopModel> = emptyList()
): IWorkshopRepository {
    @OptIn(ExperimentalTime::class)
    private var cache: Cache<String, WorkshopInMemoryDto> = object : Cache2kBuilder<String, WorkshopInMemoryDto>() {}
        .expireAfterWrite(ttl.toLongMilliseconds(), TimeUnit.MILLISECONDS) // expire/refresh after 5 minutes
        .suppressExceptions(false)
        .build()
        .also { cache ->
            initObjects.forEach {
                cache.put(it.id.id, WorkshopInMemoryDto.of(it))
            }
        }

    override suspend fun create(context: MpBeContext): MpWorkshopModel {
        val dto = WorkshopInMemoryDto.of(context.requestWorkshop, UUID.randomUUID().toString())
        val model = save(dto).toModel()
        context.responseWorkshop = model
        return model
    }

    override suspend fun read(context: MpBeContext): MpWorkshopModel {
        val id = context.requestWorkshopId
        if (id == MpWorkshopIdModel.NONE) throw MpRepoWrongIdException(id.id)
        val model = cache.get(id.id)?.toModel()?: throw MpRepoNotFoundException(id.id)
        context.responseWorkshop = model
        return model
    }

    override suspend fun update(context: MpBeContext): MpWorkshopModel {
        if (context.requestWorkshop.id == MpWorkshopIdModel.NONE) throw MpRepoWrongIdException(context.requestWorkshop.id.id)
        val model = save(WorkshopInMemoryDto.of(context.requestWorkshop)).toModel()
        context.responseWorkshop = model
        return model
    }

    override suspend fun delete(context: MpBeContext): MpWorkshopModel {
        val id = context.requestWorkshopId
        if (id == MpWorkshopIdModel.NONE) throw MpRepoWrongIdException(id.id)
        val model = cache.peekAndRemove(id.id)?.toModel()?: throw MpRepoNotFoundException(id.id)
        context.responseWorkshop = model
        return model
    }

    override suspend fun list(context: MpBeContext): Collection<MpWorkshopModel> {
        val textFilter = context.requestWorkshopFilter.text
        if (textFilter.length < 3) throw MpRepoIndexException(textFilter)
        val records = cache.asMap().filterValues {
            it.title?.contains(textFilter, true)?:false || if (context.requestWorkshopFilter.includeDescription) {
                it.description?.contains(textFilter, true) ?: false
            } else false
        }.values
        if (records.count() <= context.requestWorkshopFilter.offset)
            throw MpRepoIndexException(textFilter)
        val list = records.toList()
            .subList(
                context.requestWorkshopFilter.offset,
                if (records.count() >= context.requestWorkshopFilter.offset + context.requestWorkshopFilter.count)
                    context.requestWorkshopFilter.offset + context.requestWorkshopFilter.count
                else records.count()
            ).map { it.toModel() }
        context.responseWorkshops = list.toMutableList()
        context.pageCount = list.count().takeIf { it != 0 }
            ?.let { (records.count().toDouble() / it + 0.5).toInt() }
            ?: Int.MIN_VALUE
        return list
    }

    private suspend fun save(dto: WorkshopInMemoryDto): WorkshopInMemoryDto {
        cache.put(dto.id, dto)
        return cache.get(dto.id)
    }
}