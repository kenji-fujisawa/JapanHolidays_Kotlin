package jp.uhimania.japanholidays.data

import android.database.sqlite.SQLiteDatabase
import androidx.core.database.sqlite.transaction
import java.util.Date

internal interface LocalDataSource {
    fun getHolidays(): Map<String, String>
    fun getLastUpdated(): Date
    fun updateHolidays(holidays: Map<String, String>)
}

internal class DefaultLocalDataSource(val db: SQLiteDatabase) : LocalDataSource {
    init {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS holidays(
                date TEXT PRIMARY KEY,
                name TEXT
            )
        """.trimIndent())
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS last_updated(
                date INTEGER PRIMARY KEY
            )
        """.trimIndent())
    }

    override fun getHolidays(): Map<String, String> {
        val holidays = mutableMapOf<String, String>()

        val sql = "SELECT date, name FROM holidays"
        val cursor = db.rawQuery(sql, null)
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val date = cursor.getString(0)
            val name = cursor.getString(1)
            holidays[date] = name

            cursor.moveToNext()
        }
        cursor.close()

        return holidays
    }

    override fun getLastUpdated(): Date {
        var updated = Date(0)

        val sql = "SELECT date FROM last_updated"
        val cursor = db.rawQuery(sql, null)
        if (cursor.count != 0) {
            cursor.moveToFirst()
            updated = Date(cursor.getLong(0))
        }
        cursor.close()

        return updated
    }

    override fun updateHolidays(holidays: Map<String, String>) {
        db.transaction {
            db.execSQL("DELETE FROM holidays")
            db.execSQL("DELETE FROM last_updated")

            var sql = db.compileStatement("INSERT INTO holidays VALUES(?, ?)")
            holidays.forEach {
                sql.bindString(1, it.key)
                sql.bindString(2, it.value)
                sql.executeInsert()
            }

            sql = db.compileStatement("INSERT INTO last_updated VALUES(?)")
            sql.bindLong(1, Date().time)
            sql.executeInsert()
        }
    }
}
