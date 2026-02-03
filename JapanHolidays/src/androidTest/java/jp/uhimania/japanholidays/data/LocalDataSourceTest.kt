package jp.uhimania.japanholidays.data

import android.database.sqlite.SQLiteDatabase
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date

class LocalDataSourceTest {
    @Test
    fun testGetAndUpdate() {
        val db = SQLiteDatabase.create(null)
        val source = DefaultLocalDataSource(db)

        var results = source.getHolidays()
        assertEquals(0, results.count())

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        var updated = source.getLastUpdated()
        assertEquals(formatter.format(Date(0)), formatter.format(updated))

        val holidays = mutableMapOf(
            "2026/1/1" to "new year's day",
            "2026/1/12" to "coming of age day"
        )
        source.updateHolidays(holidays)

        results = source.getHolidays()
        assertEquals(2, results.count())
        holidays.forEach {
            assertEquals(it.value, results[it.key])
        }

        updated = source.getLastUpdated()
        assertEquals(formatter.format(Date()), formatter.format(updated))

        holidays["2026/2/11"] = "national foundation day"
        holidays["2026/2/23"] = "emperor's birthday"
        source.updateHolidays(holidays)

        results = source.getHolidays()
        assertEquals(4, results.count())
        holidays.forEach {
            assertEquals(it.value, results[it.key])
        }

        updated = source.getLastUpdated()
        assertEquals(formatter.format(Date()), formatter.format(updated))
    }
}
