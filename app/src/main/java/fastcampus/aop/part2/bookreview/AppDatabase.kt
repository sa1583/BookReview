package fastcampus.aop.part2.bookreview

import androidx.room.Database
import androidx.room.RoomDatabase
import fastcampus.aop.part2.bookreview.dao.HistoryDao
import fastcampus.aop.part2.bookreview.model.History

@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}