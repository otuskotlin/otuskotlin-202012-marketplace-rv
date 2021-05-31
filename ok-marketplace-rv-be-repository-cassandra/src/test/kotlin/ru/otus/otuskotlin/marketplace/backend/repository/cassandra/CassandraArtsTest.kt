package ru.otus.otuskotlin.marketplace.backend.repository.cassandra

import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.BeforeClass
import org.testcontainers.containers.GenericContainer
import ru.otus.otuskotlin.marketplace.backend.repository.cassandra.arts.ArtRepositoryCassandra
import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtFilterModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtIdModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpUnitTypeModel
import java.time.Duration
import kotlin.test.Test
import kotlin.test.assertEquals

class CassandraContainer : GenericContainer<CassandraContainer>("cassandra")

internal class CassandraArtsTest {

    companion object {
        private val PORT = 9042
        private val keyspace = "test_keyspace"
        private lateinit var container: CassandraContainer
        private lateinit var repo: ArtRepositoryCassandra

        @BeforeClass
        @JvmStatic
        fun tearUp() {
            container = CassandraContainer()
                .withExposedPorts(PORT)
                .withStartupTimeout(Duration.ofSeconds(300L))
                .apply {
                    start()
                }

            repo = ArtRepositoryCassandra(
                keyspaceName = keyspace,
                hosts = container.host,
                port = container.getMappedPort(PORT),
                testing = true,
                initObjects = listOf(
                    MpArtModel(
                        id = MpArtIdModel("test-id1"),
                        title = "test-Art",
                        tagIds = mutableSetOf("id1", "id2"),
                    ),
                    MpArtModel(
                        id = MpArtIdModel("test-id2"),
                        title = "test-Art1",
                        tagIds = mutableSetOf("id1", "id2"),
                    ),
                    MpArtModel(
                        id = MpArtIdModel("test-id3"),
                        title = "Art-0",
                        tagIds = mutableSetOf("id1", "id2"),
                    ),
                    MpArtModel(
                        id = MpArtIdModel("test-id4"),
                        title = "test-Art2",
                        tagIds = mutableSetOf("id1", "id2"),
                    ),
                    MpArtModel(
                        id = MpArtIdModel("test-id5"),
                        title = "Art-1",
                        tagIds = mutableSetOf("id1", "id2"),
                    ),
                )
            ).init()
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            container.close()
        }
    }

    @Test
    fun artReadTest() {
        runBlocking {
            val context = MpBeContext(
                requestArtId = MpArtIdModel("test-id2")
            )
            val model = repo.read(context)
            assertEquals(model, context.responseArt)
            assertEquals("test-Art1", model.title)
        }
    }

    @Test
    fun artListTest() {
        runBlocking {
            val context = MpBeContext(
                requestArtFilter = MpArtFilterModel(
                    text = "test",
                    offset = 1,
                    count = 2,
                )
            )
            val response = repo.list(context)
            assertEquals(response, context.responseArts)
            assertEquals(2, context.pageCount)
            assertEquals(2, response.size)
        }
    }

    @Test
    fun artCreateTest() {
        runBlocking {
            val art = MpArtModel(
                title = "created-Art",
                description = "about Art",
            )
            val context = MpBeContext(
                requestArt = art
            )
            val result = repo.create(context)
            assertEquals(result, context.responseArt)
            assertEquals("created-Art", result.title)
            assertEquals("about Art", result.description)
            val context2 = MpBeContext(requestArtId = result.id)
            repo.read(context2)
            assertEquals("created-Art", context2.responseArt.title)
            assertEquals("about Art", context2.responseArt.description)
        }
    }

    @Test
    fun artUpdateTest() {
        runBlocking {
            val art = MpArtModel(
                id = MpArtIdModel("test-id1"),
                title = "updated-Art",
                description = "about Art",
            )
            val context = MpBeContext(
                requestArt = art
            )
            val result = repo.update(context)
            assertEquals(result, context.responseArt)
            assertEquals("updated-Art", result.title)
            assertEquals("about Art", result.description)
            val context2 = MpBeContext(requestArtId = MpArtIdModel("test-id1"))
            repo.read(context2)
            assertEquals("updated-Art", context2.responseArt.title)
            assertEquals("about Art", context2.responseArt.description)
        }
    }

    @Test
    fun artDeleteTest() {
        runBlocking {
            val context = MpBeContext(
                requestArtId = MpArtIdModel("test-id2")
            )
            val model = repo.delete(context)
            assertEquals(model, context.responseArt)
            assertEquals("test-Art1", model.title)
        }
    }
}
