package mx.com.maiktmp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import mx.com.maiktmp.database.entities.PartnerDB


@Dao
interface PartnerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(vararg partner: PartnerDB): Completable

    @Query("SELECT * FROM partner")
    fun getAll(): Maybe<List<PartnerDB>>

    @Query("SELECT COUNT(*) FROM partner")
    fun count(): Single<Int>

    @Query("DELETE FROM partner")
    fun deleteAll(): Completable

    @Query("SELECT * FROM partner WHERE backed_up = :backup")
    fun findByBackup(backup: Boolean): Maybe<List<PartnerDB>>


}