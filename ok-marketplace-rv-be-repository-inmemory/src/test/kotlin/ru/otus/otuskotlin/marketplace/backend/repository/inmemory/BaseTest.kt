package ru.otus.otuskotlin.marketplace.backend.repository.inmemory

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.arts.ArtRepoInMemory
import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

internal class BaseTest {

    @OptIn(ExperimentalTime::class)
    @Test
    fun createAndGetTest() {
        val repo = ArtRepoInMemory(
            ttl = 5.toDuration(DurationUnit.MINUTES)
        )

        val art = MpArtModel(
            title = "art-title",
            description = "art-description",
            tagIds = mutableSetOf("art-tag"),
        )

        val context = MpBeContext(
            requestArt = art
        )

        runBlocking {
            val createdArt = repo.create(context)
            assertEquals("art-title", createdArt.title)
            assertEquals("art-description", createdArt.description)
            assertTrue { createdArt.tagIds.isNotEmpty() }
            context.requestArtId = createdArt.id
            val readArt = repo.read(context)
            assertEquals(createdArt.id, readArt.id)
            assertEquals("art-title", readArt.title)
            assertEquals("art-description", readArt.description)
            assertTrue {readArt.tagIds.isNotEmpty() }
        }
    }
}
