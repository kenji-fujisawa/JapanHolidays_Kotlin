package jp.uhimania.japanholidays

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.startup.Initializer
import jp.uhimania.japanholidays.data.DefaultHolidaysRepository
import jp.uhimania.japanholidays.data.DefaultLocalDataSource
import jp.uhimania.japanholidays.data.DefaultNetworkDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

internal class HolidaysInitializer : Initializer<Holidays> {
    override fun create(context: Context): Holidays {
        initJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                val file = File(context.filesDir, "jp.uhimania.japanholidays.Holidays.db")
                SQLiteDatabase.openOrCreateDatabase(file, null).use { db ->
                    val localSource = DefaultLocalDataSource(db)
                    val networkSource = DefaultNetworkDataSource()
                    val repository = DefaultHolidaysRepository(localSource, networkSource)
                    holidays.clear()
                    holidays.putAll(repository.getHolidays())
                }
            } catch (ex: Exception) {
                Log.e(TAG, ex.localizedMessage, ex)
            }
        }

        return Holidays
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return emptyList()
    }

    companion object {
        var initJob: Job? = null

        val TAG = HolidaysInitializer::class.simpleName
    }
}
