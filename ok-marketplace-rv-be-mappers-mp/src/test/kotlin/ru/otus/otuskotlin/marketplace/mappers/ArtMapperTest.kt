package ru.otus.otuskotlin.marketplace.mappers

import org.junit.Test
import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.transport.models.arts.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ArtMapperTest {

    @Test
    fun mappingTestRequestArtCreate() {
        val request = MpRequestArtCreate(
            createData = MpArtCreateDto(
                title = "some_title",
                description = "some_description",
                tagIds = setOf("tag1","tag2")
            )
        )

        val context =MpBeContext()
        context.setQuery(request)
        assertEquals("some_title", context.requestArt.title)
        assertEquals("some_description", context.requestArt.description)
        assertTrue { context.requestArt.tagIds.contains("tag2") }
    }

    @Test
    fun mappingTestRequestArtRead() {
        val request = MpRequestArtRead(
            artId = "some_id"
        )

        val context =MpBeContext()
        context.setQuery(request)
        assertEquals("some_id", context.requestArtId.id)
    }

    @Test
    fun mappingTestRequestArtUpdate() {
        val request = MpRequestArtUpdate(
            updateData = MpArtUpdateDto(
                id = "some_id",
                title = "some_title",
                description = "some_description",
                tagIds = setOf("tag1","tag2")
            )
        )

        val context =MpBeContext()
        context.setQuery(request)
        assertEquals("some_id", context.requestArt.id.id)
        assertEquals("some_title", context.requestArt.title)
        assertEquals("some_description", context.requestArt.description)
        assertTrue { context.requestArt.tagIds.contains("tag2") }
    }

    @Test
    fun mappingTestRequestArtDelete() {
        val request = MpRequestArtDelete(
            artId = "some_id"
        )

        val context =MpBeContext()
        context.setQuery(request)
        assertEquals("some_id", context.requestArtId.id)
    }
}