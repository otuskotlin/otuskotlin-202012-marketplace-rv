package ru.otus.otuskotlin.marketplace.transport.models

import kotlinx.serialization.json.Json
import ru.otus.otuskotlin.marketplace.transport.models.arts.MpArtCreateDto
import ru.otus.otuskotlin.marketplace.transport.models.arts.MpRequestArtCreate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SerializationTest {

    @Test
    fun serializationMpRequestArtCreate () {
        val json = Json {
            prettyPrint = true
        }
        val art = MpRequestArtCreate (
            requestId = "some_id",
            createData = MpArtCreateDto(
                title = "some-art",
                description = "some-description",
                tagIds = setOf("tag1", "tag2")
            )
        )

        val serializedArt = json.encodeToString(MpRequestArtCreate.serializer(), art)
        assertTrue { serializedArt.contains("some-art") }
        val deserializedArt = json.decodeFromString(MpRequestArtCreate.serializer(), serializedArt)
        assertEquals("some-art",deserializedArt.createData?.title)
    }
}