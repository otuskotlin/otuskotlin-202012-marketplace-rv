package ru.otus.otuskotlin.marketplace.common.backend.repositories

import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.common.backend.models.MpWorkshopModel

interface IWorkshopRepository {

    suspend fun read(context: MpBeContext): MpWorkshopModel
    suspend fun create(context: MpBeContext): MpWorkshopModel
    suspend fun update(context: MpBeContext): MpWorkshopModel
    suspend fun delete(context: MpBeContext): MpWorkshopModel
    suspend fun list(context: MpBeContext): Collection<MpWorkshopModel>

    companion object {
        val NONE = object : IWorkshopRepository {
            override suspend fun read(context: MpBeContext): MpWorkshopModel {
                TODO("Not yet implemented")
            }

            override suspend fun create(context: MpBeContext): MpWorkshopModel {
                TODO("Not yet implemented")
            }

            override suspend fun update(context: MpBeContext): MpWorkshopModel {
                TODO("Not yet implemented")
            }

            override suspend fun delete(context: MpBeContext): MpWorkshopModel {
                TODO("Not yet implemented")
            }

            override suspend fun list(context: MpBeContext): Collection<MpWorkshopModel> {
                TODO("Not yet implemented")
            }
        }
    }
}
