package mx.com.maiktmp.skilltestphndr.base

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import mx.com.maiktmp.database.dao.PartnerDao
import mx.com.maiktmp.database.entities.PartnerDB

class BackupBase(private val partnerDao: PartnerDao) {
    private val db = Firebase.firestore

    @SuppressLint("CheckResult")
    fun attemptBackup() {
        partnerDao.findByBackup(false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    for (partnerDB in it) {
                        partnerDB.backedUp = true
                        backupInFireStore(partnerDB)
                    }
                },
                {}
            )
    }

    private fun backupInFireStore(partner: PartnerDB) {
        db.collection("partners")
            .add(partner)
            .addOnSuccessListener { _ ->
                partnerDao.upsert(partner)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({}, {})
            }
            .addOnFailureListener { e ->
                Log.w(this::class.java.name, "Error adding document", e)
            }
    }

}