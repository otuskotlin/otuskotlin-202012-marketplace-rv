import io.ktor.http.*
import io.ktor.server.testing.*
import ru.otus.otuskotlin.controllers.RestEndpoints
import ru.otus.otuskotlin.jsonConfig
import ru.otus.otuskotlin.marketplace.transport.models.arts.MpArtFilterDto
import ru.otus.otuskotlin.marketplace.transport.models.arts.MpRequestArtList
import ru.otus.otuskotlin.marketplace.transport.models.arts.MpResponseArtList
import ru.otus.otuskotlin.marketplace.transport.models.common.MpMessage
import ru.otus.otuskotlin.marketplace.transport.models.common.MpWorkModeDto
import ru.otus.otuskotlin.marketplace.transport.models.common.ResponseStatusDto
import ru.otus.otuskotlin.module
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class ArtListValidationTest {

    @Test
    fun `non-empty list must success`() {
        withTestApplication({ module(testing = true)}) {
            handleRequest(HttpMethod.Post, RestEndpoints.artList) {
                val body = MpRequestArtList(
                    requestId = "321",
                    filterData = MpArtFilterDto(

                    ),
                    debug = MpRequestArtList.Debug(
                        mode = MpWorkModeDto.TEST,
                        stubCase = MpRequestArtList.StubCase.SUCCESS
                    )
                )

                val bodyString = jsonConfig.encodeToString(MpMessage.serializer(), body)
                println("REQUEST JSON: $bodyString")
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                val res = (jsonConfig.decodeFromString(MpMessage.serializer(), jsonString) as? MpResponseArtList)
                    ?: fail("Incorrect response format")

                assertEquals(ResponseStatusDto.SUCCESS, res.status)
                assertEquals("321", res.onRequest)
            }
        }
    }

    @Test
    fun `bad json must fail`() {
        withTestApplication({ module(testing = true)}) {
            handleRequest(HttpMethod.Post, RestEndpoints.artList) {
                val bodyString = "{"
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                val res = (jsonConfig.decodeFromString(MpMessage.serializer(), jsonString) as? MpResponseArtList)
                    ?: fail("Incorrect response format")

                assertEquals(ResponseStatusDto.BAD_REQUEST, res.status)
                assertTrue {
                    res.errors?.find { it.message?.toLowerCase()?.contains("syntax") == true } != null
                }
            }
        }
    }
}
