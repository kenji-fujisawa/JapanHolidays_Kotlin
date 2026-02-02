package jp.uhimania.japanholidays.data

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

class HolidaysRepositoryTest {
    @Test
    fun testGetHolidays() = runTest {
        val localSource = FakeLocalDataSource()
        val networkSource = FakeNetworkDataSource()
        val repository = DefaultHolidaysRepository(localSource, networkSource)

        var holidays = repository.getHolidays()
        assertEquals(2, holidays.count())
        localSource._holidays.forEach {
            assertEquals(it.value, holidays[it.key])
        }
        assertEquals(0, localSource.updated.count())

        localSource._lastUpdated = Date(Date().time - 29L * 24 * 60 * 60 * 1000)
        holidays = repository.getHolidays()
        assertEquals(2, holidays.count())
        localSource._holidays.forEach {
            assertEquals(it.value, holidays[it.key])
        }
        assertEquals(0, localSource.updated.count())

        localSource._lastUpdated = Date(Date().time - 30L * 24 * 60 * 60 * 1000)
        holidays = repository.getHolidays()
        assertEquals(3, holidays.count())
        networkSource._holidays.forEach {
            assertEquals(it.value, holidays[it.key])
        }
        assertEquals(3, localSource.updated.count())
        networkSource._holidays.forEach {
            assertEquals(it.value, localSource.updated[it.key])
        }
    }

    class FakeLocalDataSource : LocalDataSource {
        val _holidays = mapOf(
            "2026/1/1" to "new year's day",
            "2026/1/12" to "coming of age day"
        )
        override fun getHolidays(): Map<String, String> {
            return _holidays
        }

        var _lastUpdated = Date()
        override fun getLastUpdated(): Date {
            return _lastUpdated
        }

        var updated = mutableMapOf<String, String>()
        override fun updateHolidays(holidays: Map<String, String>) {
            updated.clear()
            updated.putAll(holidays)
        }
    }

    class FakeNetworkDataSource : NetworkDataSource {
        val _holidays = mapOf(
            "2026/1/1" to "new year's day",
            "2026/1/12" to "coming of age day",
            "2026/2/11" to "national foundation day"
        )
        override suspend fun getHolidays(): Map<String, String> {
            return _holidays
        }
    }
}