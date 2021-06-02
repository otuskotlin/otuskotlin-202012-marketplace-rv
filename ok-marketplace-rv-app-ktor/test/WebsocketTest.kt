import io.ktor.http.cio.websocket.*
import io.ktor.server.testing.*
import kotlinx.coroutines.withTimeoutOrNull
import ru.otus.otuskotlin.jsonConfig
import ru.otus.otuskotlin.marketplace.transport.models.arts.MpRequestArtList
import ru.otus.otuskotlin.marketplace.transport.models.arts.MpResponseArtList
import ru.otus.otuskotlin.marketplace.transport.models.common.MpMessage
import ru.otus.otuskotlin.marketplace.transport.models.common.ResponseStatusDto
import ru.otus.otuskotlin.module
import kotlin.test.Test
import kotlin.test.assertEquals

internal class WebsocketTest {

     @Test
     fun artListTest() {
           withTestApplication({ module(testing = true) }) {
               handleWebSocketConversation("/ws") { incoming, outgoing ->
                   val query = MpRequestArtList(
                       requestId = "123",
                       debug = MpRequestArtList.Debug(
                           stubCase = MpRequestArtList.StubCase.SUCCESS
                       )
                   )
                   withTimeoutOrNull(250L) {
                       while (true) {
                           val respJson =(incoming.receive() as Frame.Text).readText()
                           println("GOT INIT RESPONSE: $respJson")
                       }
                   }
                   val requestJson = jsonConfig.encodeToString(MpMessage.serializer(), query)
                   outgoing.send(Frame.Text(requestJson))
                   val respJson =(incoming.receive() as Frame.Text).readText()
                   println("RESPONSE: $respJson")
                   val response = jsonConfig.decodeFromString(MpMessage.serializer(),respJson) as MpResponseArtList
                   assertEquals("123", response.onRequest)

               }
           }
       }
    @Test
    fun artListErrorTest() {
        withTestApplication({ module(testing = true) }) {
            handleWebSocketConversation("/ws") { incoming, outgoing ->
                withTimeoutOrNull(250L) {
                    while (true) {
                        val respJson =(incoming.receive() as Frame.Text).readText()
                        println("GOT INIT RESPONSE: $respJson")
                    }
                }
                val requestJson = """{"type":"123"}"""
                outgoing.send(Frame.Text(requestJson))
                val respJson =(incoming.receive() as Frame.Text).readText()
                println("RESPONSE: $respJson")
                val response = jsonConfig.decodeFromString(MpMessage.serializer(),respJson) as MpResponseArtList
                assertEquals(ResponseStatusDto.BAD_REQUEST, response.status)

            }
        }
    }
}
