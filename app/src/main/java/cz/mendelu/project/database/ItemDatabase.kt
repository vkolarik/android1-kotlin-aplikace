package cz.mendelu.project.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cz.mendelu.project.model.Accomplishment
import cz.mendelu.project.model.Item
import cz.mendelu.project.model.Routine

@Database(entities = [Item::class, Routine::class, Accomplishment::class], version = 2, exportSchema = true)
abstract class ItemDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao
    companion object {
        private var INSTANCE: ItemDatabase? = null
        fun getDatabase(context: Context): ItemDatabase {
            if (INSTANCE == null) {
                synchronized(ItemDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            ItemDatabase::class.java, "item_database"
                        )/*.fallbackToDestructiveMigration()*/.build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}