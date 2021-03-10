package ru.otus.otuskotlin.marketplace.mappers

import org.junit.Test
import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.transport.models.workshops.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WorkshopMapperTest {

    @Test
    fun mappingTestRequestWorkshopCreate() {
        val request = MpRequestWorkshopCreate(
            createData = MpWorkshopCreateDto(
                title = "some_title",
                description = "some_description",
                tagIds = setOf("tag1","tag2")
            )
        )

        val context = MpBeContext()
        context.setQuery(request)
        assertEquals("some_title", context.requestWorkshop.title)
        assertEquals("some_description", context.requestWorkshop.description)
        assertTrue { context.requestWorkshop.tagIds.contains("tag2") }
    }

    @Test
    fun mappingTestRequestWorkshopRead() {
        val request = MpRequestWorkshopRead(
            workshopId = "some_id"
        )

        val context = MpBeContext()
        context.setQuery(request)
        assertEquals("some_id", context.requestWorkshopId.id)
    }

    @Test
    fun mappingTestRequestWorkshopUpdate() {
        val request = MpRequestWorkshopUpdate(
            updateData = MpWorkshopUpdateDto(
                id = "some_id",
                title = "some_title",
                description = "some_description",
                tagIds = setOf("tag1","tag2")
            )
        )

        val context = MpBeContext()
        context.setQuery(request)
        assertEquals("some_id", context.requestWorkshop.id.id)
        assertEquals("some_title", context.requestWorkshop.title)
        assertEquals("some_description", context.requestWorkshop.description)
        assertTrue { context.requestWorkshop.tagIds.contains("tag2") }
    }

    @Test
    fun mappingTestRequestWorkshopDelete() {
        val request = MpRequestWorkshopDelete(
            workshopId = "some_id"
        )

        val context = MpBeContext()
        context.setQuery(request)
        assertEquals("some_id", context.requestWorkshopId.id)
    }
}