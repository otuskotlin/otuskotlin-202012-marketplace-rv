package ru.otus.otuskotlin.marketplace.rv.business.logic.backend

import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.rv.business.logic.backend.pipelines.*

class ArtCrud {
    suspend fun create(context: MpBeContext) {
        ArtCreate.execute(context.apply (this::configureContext))
    }

    suspend fun read(context: MpBeContext) {
        ArtRead.execute(context.apply (this::configureContext))
    }

    suspend fun update(context: MpBeContext) {
        ArtUpdate.execute(context.apply (this::configureContext))
    }

    suspend fun delete(context: MpBeContext) {
        ArtDelete.execute(context.apply (this::configureContext))
    }

    suspend fun filter(context: MpBeContext) {
        ArtFilter.execute(context.apply (this::configureContext))
    }

    private fun configureContext(context: MpBeContext) {

    }
}