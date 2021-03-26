package ru.otus.otuskotlin.controllers

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.util.pipeline.*
import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.marketplace.transport.models.common.MpErrorDto
import ru.otus.otuskotlin.marketplace.transport.models.common.MpMessage
import ru.otus.otuskotlin.marketplace.transport.models.common.MpResponseStatusDto
import ru.otus.otuskotlin.marketplace.transport.models.workshops.*

class WorkshopController {

    private val log = LoggerFactory.getLogger(this::class.java)!!

    suspend fun create(pipeline: PipelineContext<Unit, ApplicationCall>) {
        try {
            val query = pipeline.call.receive<MpMessage>() as MpRequestWorkshopCreate

            val response: MpMessage = MpResponseWorkshopCreate(
                responseId = "1",
                onRequestId = query.requestId,
                debug = query.debug,
                responseStatus = MpResponseStatusDto.SUCCESS,
                workshop = mockUpdate(
                    id = "1",
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
            val query = pipeline.call.receive<MpMessage>() as MpRequestWorkshopRead

            val response: MpMessage = MpResponseWorkshopRead(
                responseId = "1",
                onRequestId = query.requestId,
                responseStatus = MpResponseStatusDto.SUCCESS,
                workshop = mockRead(query.workshopId?: ""),
            )
            pipeline.call.respond(response)
        } catch (e: Throwable) {
            log.error("can't read the workshop", e)
        }
    }

    suspend fun update(pipeline: PipelineContext<Unit, ApplicationCall>) {
        try {
            val query = pipeline.call.receive<MpMessage>() as MpRequestWorkshopUpdate
            val id = query.updateData?.id

            val response: MpMessage = if (id != null)
                MpResponseWorkshopUpdate(
                    responseId = "2",
                    onRequestId = query.requestId,
                    debug = query.debug,
                    responseStatus = MpResponseStatusDto.SUCCESS,
                    workshop = mockUpdate(
                        id = "tartartar",
                        title = query.updateData?.title,
                        description = query.updateData?.description,
                    )
                )
            else
                MpResponseWorkshopUpdate(
                    responseId = "3",
                    onRequestId = query.requestId,
                    debug = query.debug,
                    responseStatus = MpResponseStatusDto.SUCCESS,
                    errors = listOf(
                        MpErrorDto(
                            code = "bad id",
                            message = "wprkshop $id didn't found",
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
            val query = pipeline.call.receive<MpMessage>() as MpRequestWorkshopDelete

            val response: MpMessage = MpResponseWorkshopDelete(
                responseId = "1",
                onRequestId = query.requestId,
                responseStatus = MpResponseStatusDto.SUCCESS,
                workshop = mockRead(query.workshopId?: ""),
                deleted = true,
            )
            pipeline.call.respond(response)
        } catch (e: Throwable) {
            log.error("can't delete the workshop", e)
        }
    }

    companion object {
        fun mockUpdate(
            id: String?,
            title: String?,
            description: String?,
        ) = MpWorkshopDto(
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