package ru.otus.otuskotlin.marketplace.common.backend.repositories

import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtModel

interface IArtRepository {
    suspend fun read(context: MpBeContext): MpArtModel
    suspend fun create(context: MpBeContext): MpArtModel
    suspend fun update(context: MpBeContext): MpArtModel
    suspend fun delete(context: MpBeContext): MpArtModel
    suspend fun list(context: MpBeContext): Collection<MpArtModel>


    companion object {
        val NONE = object : IArtRepository {
            override suspend fun read(context: MpBeContext): MpArtModel {
                TODO("Not yet implemented")
            }

            override suspend fun create(context: MpBeContext): MpArtModel {
                TODO("Not yet implemented")
            }

            override suspend fun update(context: MpBeContext): MpArtModel {
                TODO("Not yet implemented")
            }

            override suspend fun delete(context: MpBeContext): MpArtModel {
                TODO("Not yet implemented")
            }

            override suspend fun list(context: MpBeContext): Collection<MpArtModel> {
                TODO("Not yet implemented")
            }
        }
    }
}
