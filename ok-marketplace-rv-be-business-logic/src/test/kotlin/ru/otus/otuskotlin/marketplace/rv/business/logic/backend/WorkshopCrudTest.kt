package ru.otus.otuskotlin.marketplace.rv.business.logic.backend

import org.junit.Test
import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContextStatus
import ru.otus.otuskotlin.marketplace.common.backend.models.*
import runBlockingTest
import kotlin.test.assertEquals


class WorkshopCrudTest {
    @Test
    fun create() {
        val givenCrud = WorkshopCrud()
        val givenContext = MpBeContext(
            stubCase = MpStubCase.WORKSHOP_CREATE_SUCCESS,
            requestWorkshop= MpWorkshopModel(
                title = "title",
                description = "description",
                tagIds = mutableSetOf("tag1", "tag2"),
            )
        )
        runBlockingTest { givenCrud.create(givenContext) }

        assertEquals(MpBeContextStatus.SUCCESS, givenContext.status)
        with(givenContext.responseWorkshop) {
            assertEquals(MpWorkshopIdModel("some_id"), id)
            assertEquals("some_title", title)
            assertEquals("some_description", description)
            assertEquals(setOf("tag1", "tag2"), tagIds)
        }
    }

    @Test
    fun read() {
        val givenCrud = WorkshopCrud()
        val givenContext = MpBeContext(
            stubCase = MpStubCase.WORKSHOP_READ_SUCCESS,
            requestWorkshop = MpWorkshopModel(
                id = MpWorkshopIdModel("some_id"),
                title = "some_title",
                description = "some_description",
                tagIds = mutableSetOf("tag1", "tag2"),
            )
        )

        runBlockingTest { givenCrud.read(givenContext) }

        assertEquals(MpBeContextStatus.SUCCESS, givenContext.status)
        with(givenContext.responseWorkshop) {
            assertEquals(MpWorkshopIdModel("some_id"), id)
            assertEquals("some_title", title)
            assertEquals("some_description", description)
            assertEquals(setOf("tag1", "tag2"), tagIds)
        }
    }

    @Test
    fun update() {
        val givenCrud = WorkshopCrud()
        val givenContext = MpBeContext(
            stubCase = MpStubCase.WORKSHOP_UPDATE_SUCCESS,
            requestWorkshop = MpWorkshopModel(
                id = MpWorkshopIdModel("some_id"),
                title = "some_title",
                description = "some_description",
                tagIds = mutableSetOf("tag1", "tag2"),
            )
        )

        runBlockingTest { givenCrud.update(givenContext) }

        assertEquals(MpBeContextStatus.SUCCESS, givenContext.status)
        with(givenContext.responseWorkshop) {
            assertEquals(MpWorkshopIdModel("some_id"), id)
            assertEquals("some_title", title)
            assertEquals("some_description", description)
            assertEquals(setOf("tag1", "tag2"), tagIds)
        }
    }

    @Test
    fun delete() {
        val givenCrud = WorkshopCrud()
        val givenContext = MpBeContext(
            stubCase = MpStubCase.WORKSHOP_DELETE_SUCCESS,
            requestWorkshopId = MpWorkshopIdModel("some_id")
        )

        runBlockingTest { givenCrud.delete(givenContext) }

        assertEquals(MpBeContextStatus.SUCCESS, givenContext.status)
        with(givenContext.responseWorkshop) {
            assertEquals(MpWorkshopIdModel("some_id"), id)
            assertEquals("some_title", title)
            assertEquals("some_description", description)
            assertEquals(setOf("tag1", "tag2"), tagIds)
        }
    }

    @Test
    fun filter() {
        val givenCrud = WorkshopCrud()
        val givenContext = MpBeContext(
            stubCase = MpStubCase.WORKSHOP_FILTER_SUCCESS,
            requestWorkshopFilter = MpWorkshopFilterModel(text = "test")
        )

        runBlockingTest { givenCrud.filter(givenContext) }

        assertEquals(MpBeContextStatus.SUCCESS, givenContext.status)
        assertEquals(1, givenContext.responseWorkshops.size)
        with(givenContext.responseWorkshops[0]) {
            assertEquals(MpWorkshopIdModel("some_id"), id)
            assertEquals("some_title", title)
            assertEquals("some_description", description)
            assertEquals(setOf("tag1", "tag2"), tagIds)
        }
    }
}