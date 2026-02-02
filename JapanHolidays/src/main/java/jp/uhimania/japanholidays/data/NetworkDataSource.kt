package jp.uhimania.japanholidays.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

internal interface NetworkDataSource {
    suspend fun getHolidays(): Map<String, String>
}

internal class DefaultNetworkDataSource(
    val engine: HttpClientEngine = Android.create()
) : NetworkDataSource {
    override suspend fun getHolidays(): Map<String, String> {
        val client = HttpClient(engine)
        val response = client.get("https://www8.cao.go.jp/chosei/shukujitsu/syukujitsu.csv")
        return response.bodyAsText(charset("Shift_JIS"))
            .lines()
            .drop(1)
            .filter { it.contains(',') }
            .map { it.split(',') }
            .associateBy({ it[0] }, { it[1] })
    }
}
