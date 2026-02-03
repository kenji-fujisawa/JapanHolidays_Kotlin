package jp.uhimania.japanholidays.data

import java.util.Date

internal interface HolidaysRepository {
    suspend fun getHolidays(): Map<String, String>
}

internal class DefaultHolidaysRepository(
    val localSource: LocalDataSource,
    val networkSource: NetworkDataSource
) : HolidaysRepository {
    override suspend fun getHolidays(): Map<String, String> {
        val updated = localSource.getLastUpdated()
        val days30 = 30L * 24 * 60 * 60 * 1000
        if (Date().time - updated.time < days30) {
            return localSource.getHolidays()
        } else {
            val holidays = networkSource.getHolidays()
            localSource.updateHolidays(holidays)
            return holidays
        }
    }
}
