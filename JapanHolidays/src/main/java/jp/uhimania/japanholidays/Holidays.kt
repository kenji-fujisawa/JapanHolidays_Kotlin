package jp.uhimania.japanholidays

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

object Holidays {
    fun isHoliday(year: Int, month: Int, day: Int): Boolean {
        val key = "$year/$month/$day"
        return holidays.keys.contains(key)
    }

    fun isHoliday(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return isHoliday(year, month, day)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isHoliday(date: LocalDate): Boolean {
        val year = date.year
        val month = date.monthValue
        val day = date.dayOfMonth
        return isHoliday(year, month, day)
    }

    fun getName(year: Int, month: Int, day: Int): String? {
        val key = "$year/$month/$day"
        return holidays[key]
    }

    fun getName(date: Date): String? {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return getName(year, month, day)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getName(date: LocalDate): String? {
        val year = date.year
        val month = date.monthValue
        val day = date.dayOfMonth
        return getName(year, month, day)
    }

    suspend fun joinInit() {
        HolidaysInitializer.initJob?.join()
    }

    fun cancelInit() {
        HolidaysInitializer.initJob?.cancel()
    }
}
