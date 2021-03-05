package mx.com.maiktmp.skilltestphndr

import android.app.Application
import mx.com.maiktmp.database.DBSkillTestPhndr

class SkillTestPhndrApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initDatabase()
    }


    private fun initDatabase() {
        DBSkillTestPhndr.createDatabase(this)
    }
}