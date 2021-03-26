package ru.otus.otuskotlin.controllers

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.util.pipeline.*
import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.marketplace.transport.models.arts.*
import ru.otus.otuskotlin.marketplace.transport.models.common.MpErrorDto
import ru.otus.otuskotlin.marketplace.transport.models.common.MpMessage
import ru.otus.otuskotlin.marketplace.transport.models.common.MpResponseStatusDto

class ArtController {

    private val log = LoggerFactory.getLogger(this::class.java)!!

    suspend fun create(pipeline: PipelineContext<Unit, ApplicationCall>) {
        try {
            val query = pipeline.call.receive<MpMessage>() as MpRequestArtCreate

            val response: MpMessage = MpResponseArtCreate(
                responseId = "1",
                onRequestId = query.requestId,
                debug = query.debug,
                responseStatus = MpResponseStatusDto.SUCCESS,
                art = mockUpdate(
                    id = "artartart",
                    title = query.createData?.title,
                    description = query.createData?.description,
                )
            )
            pipeline.call.respond(response)
        } catch (e: Throwable) {
            log.error("create chain error", e)
        }
    }

    suspend fun read(pipeline: PipelineContext<Unit, ApplicationCall>) {
        try {
            val query = pipeline.call.receive<MpMessage>() as MpRequestArtRead

            val response: MpMessage = MpResponseArtRead(
                responseId = "1",
                onRequestId = query.requestId,
                responseStatus = MpResponseStatusDto.SUCCESS,
                art = mockRead(query.artId?: ""),
            )
            pipeline.call.respond(response)
        } catch (e: Throwable) {
            log.error("can't read the art", e)
        }
    }

    suspend fun update(pipeline: PipelineContext<Unit, ApplicationCall>) {
        try {
            val query = pipeline.call.receive<MpMessage>() as MpRequestArtUpdate
            val id = query.updateData?.id

            val response: MpMessage = if (id != null)
                MpResponseArtUpdate(
                    responseId = "2",
                    onRequestId = query.requestId,
                    debug = query.debug,
                    responseStatus = MpResponseStatusDto.SUCCESS,
                    art = mockUpdate(
                        id = "tartartar",
                        title = query.updateData?.title,
                        description = query.updateData?.description,
                    )
                )
            else
                MpResponseArtUpdate(
                    responseId = "3",
                    onRequestId = query.requestId,
                    debug = query.debug,
                    responseStatus = MpResponseStatusDto.SUCCESS,
                    errors = listOf(
                        MpErrorDto(
                            code = "bad id",
                            message = "art $id didn't found",
                            field = "id",
                            level = MpErrorDto.ErrorLevelDto.ERROR
                        )
                    )
                )
            pipeline.call.respond(response)
        } catch (e: Throwable) {
            log.error("create art error", e)
        }
    }

    suspend fun delete(pipeline: PipelineContext<Unit, ApplicationCall>) {
        try {
            val query = pipeline.call.receive<MpMessage>() as MpRequestArtDelete

            val response: MpMessage = MpResponseArtDelete(
                responseId = "1",
                onRequestId = query.requestId,
                responseStatus = MpResponseStatusDto.SUCCESS,
                art = mockRead(query.artId?: ""),
                deleted = true,
            )
            pipeline.call.respond(response)
        } catch (e: Throwable) {
            log.error("can't delete the art", e)
        }
    }

    companion object {
        fun mockUpdate(
            id: String?,
            title: String?,
            description: String?,
        ) = MpArtDto(
            id = id,
            title = title,
            description = description,
            tagIds = setOf("tag1", "tag2")
        )

        fun mockRead(id: String) = mockUpdate(
            id = id,
            title = "title $id",
            description = "description $id",
        )
    }
}