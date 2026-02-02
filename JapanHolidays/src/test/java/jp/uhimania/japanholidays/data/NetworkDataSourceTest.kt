package jp.uhimania.japanholidays.data

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkDataSourceTest {
    private val responseBody = """
        国民の祝日・休日月日,国民の祝日・休日名称
        1955/1/1,元日
        1955/1/15,成人の日
        1955/3/21,春分の日

    """.trimIndent()

    @Test
    fun testGetHolidays() = runTest {
        val engine = MockEngine {
            respond(
                content = ByteReadChannel(responseBody, charset("Shift_JIS")),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "text/csv")
            )
        }
        val source = DefaultNetworkDataSource(engine)
        val holidays = source.getHolidays()
        assertEquals(3, holidays.count())
        assertEquals("元日", holidays["1955/1/1"])
        assertEquals("成人の日", holidays["1955/1/15"])
        assertEquals("春分の日", holidays["1955/3/21"])
    }
}
