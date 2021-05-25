package ru.otus.otuskotlin.marketplace.rv.business.logic.backend

import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.common.backend.repositories.IArtRepository
import ru.otus.otuskotlin.marketplace.common.backend.repositories.IWorkshopRepository
import ru.otus.otuskotlin.marketplace.rv.business.logic.backend.pipelines.*

class WorkshopCrud(
    private val workshopRepoTest: IWorkshopRepository = IWorkshopRepository.NONE,
    private val artRepoTest: IArtRepository = IArtRepository.NONE,
) {
    suspend fun create(context: MpBeContext) {
        WorkshopCreate.execute(context.apply (this::configureContext))
    }

    suspend fun read(context: MpBeContext) {
        WorkshopRead.execute(context.apply (this::configureContext))
    }

    suspend fun update(context: MpBeContext) {
        WorkshopUpdate.execute(context.apply (this::configureContext))
    }

    suspend fun delete(context: MpBeContext) {
        WorkshopDelete.execute(context.apply (this::configureContext))
    }

    suspend fun filter(context: MpBeContext) {
        WorkshopFilter.execute(context.apply (this::configureContext))
    }

    private fun configureContext(context: MpBeContext) {
        context.workshopRepoTest = workshopRepoTest
        context.artRepoTest = artRepoTest
    }
}