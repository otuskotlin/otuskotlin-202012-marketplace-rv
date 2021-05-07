package ru.otus.otuskotlin.marketplace.rv.business.logic.backend

import org.junit.Test
import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContextStatus
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtFilterModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtIdModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpStubCase
import runBlockingTest
import kotlin.test.assertEquals

class ArtCrudTest {
    @Test
    fun create() {
        val givenCrud = ArtCrud()
        val givenContext = MpBeContext(
            stubCase = MpStubCase.ART_CREATE_SUCCESS,
            requestArt = MpArtModel(
                title = "title",
                description = "description",
                tagIds = mutableSetOf("tag1", "tag2"),
            )
        )
        runBlockingTest { givenCrud.create(givenContext) }

        assertEquals(MpBeContextStatus.SUCCESS, givenContext.status)
        with(givenContext.responseArt) {
            assertEquals(MpArtIdModel("some_id"), id)
            assertEquals("some_title", title)
            assertEquals("some_description", description)
            assertEquals(setOf("tag1", "tag2"), tagIds)
        }
    }

    @Test
    fun read() {
        val givenCrud = ArtCrud()
        val givenContext = MpBeContext(
            stubCase = MpStubCase.ART_READ_SUCCESS,
            requestArt = MpArtModel(
                id = MpArtIdModel("some_id"),
                title = "some_title",
                description = "some_description",
                tagIds = mutableSetOf("tag1", "tag2"),
            )
        )

        runBlockingTest { givenCrud.read(givenContext) }

        assertEquals(MpBeContextStatus.SUCCESS, givenContext.status)
        with(givenContext.responseArt) {
            assertEquals(MpArtIdModel("some_id"), id)
            assertEquals("some_title", title)
            assertEquals("some_description", description)
            assertEquals(setOf("tag1", "tag2"), tagIds)
        }
    }

    @Test
    fun update() {
        val givenCrud = ArtCrud()
        val givenContext = MpBeContext(
            stubCase = MpStubCase.ART_UPDATE_SUCCESS,
            requestArt = MpArtModel(
                id = MpArtIdModel("some_id"),
                title = "some_title",
                description = "some_description",
                tagIds = mutableSetOf("tag1", "tag2"),
                )
        )

        runBlockingTest { givenCrud.update(givenContext) }

        assertEquals(MpBeContextStatus.SUCCESS, givenContext.status)
        with(givenContext.responseArt) {
            assertEquals(MpArtIdModel("some_id"), id)
            assertEquals("some_title", title)
            assertEquals("some_description", description)
            assertEquals(setOf("tag1", "tag2"), tagIds)
        }
    }

    @Test
    fun delete() {
        val givenCrud = ArtCrud()
        val givenContext = MpBeContext(
            stubCase = MpStubCase.ART_DELETE_SUCCESS,
            requestArtId = MpArtIdModel("some_id")
        )

        runBlockingTest { givenCrud.delete(givenContext) }

        assertEquals(MpBeContextStatus.SUCCESS, givenContext.status)
        with(givenContext.responseArt) {
            assertEquals(MpArtIdModel("some_id"), id)
            assertEquals("some_title", title)
            assertEquals("some_description", description)
            assertEquals(setOf("tag1", "tag2"), tagIds)
        }
    }

    @Test
    fun filter() {
        val givenCrud = ArtCrud()
        val givenContext = MpBeContext(
            stubCase = MpStubCase.ART_FILTER_SUCCESS,
            requestArtFilter = MpArtFilterModel(text = "test")
        )

        runBlockingTest { givenCrud.filter(givenContext) }

        assertEquals(MpBeContextStatus.SUCCESS, givenContext.status)
        assertEquals(1, givenContext.responseArts.size)
        with(givenContext.responseArts[0]) {
            assertEquals(MpArtIdModel("some_id"), id)
            assertEquals("some_title", title)
            assertEquals("some_description", description)
            assertEquals(setOf("tag1", "tag2"), tagIds)
        }
    }
}