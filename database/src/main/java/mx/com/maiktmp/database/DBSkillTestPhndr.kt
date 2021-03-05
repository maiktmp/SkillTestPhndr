package mx.com.maiktmp.database

import android.content.Context
import androidx.room.*
import mx.com.maiktmp.database.dao.PartnerDao
import mx.com.maiktmp.database.entities.PartnerDB

@Database(
    entities = [
        PartnerDB::class
    ],
    version = 3,
    exportSchema = false
)
abstract class DBSkillTestPhndr : RoomDatabase() {

    abstract fun partnerDao(): PartnerDao

    companion object {
        @Volatile
        lateinit var db: DBSkillTestPhndr

        fun createDatabase(context: Context) {
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DBSkillTestPhndr::class.java,
                    "DBSkillTestPhndr.db"
                ).fallbackToDestructiveMigration()
                    .build()
                db = instance
                instance
            }
        }
    }
}