package mx.com.maiktmp.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import mx.com.maiktmp.database.entities.LocationDB

@Entity(tableName = "partner")
data class PartnerDB(

    @PrimaryKey(autoGenerate = true)
    val idLocal: Long? = null,

    val id: Long? = null,

    val name: String? = null,

    val mail: String? = null,

    @ColumnInfo(name = "backed_up")
    var backedUp: Boolean = false,

    @Embedded
    val location: LocationDB? = null

)