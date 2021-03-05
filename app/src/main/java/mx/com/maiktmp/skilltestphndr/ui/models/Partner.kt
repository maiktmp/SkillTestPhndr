package mx.com.maiktmp.skilltestphndr.ui.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import mx.com.maiktmp.database.entities.LocationDB

data class Partner(

    val idLocal: Long? = null,

    val id: Long? = null,

    var name: String? = null,

    var mail: String? = null,

    var location: Location? = null

)