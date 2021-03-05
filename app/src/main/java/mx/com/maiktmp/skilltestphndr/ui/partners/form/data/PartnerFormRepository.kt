package mx.com.maiktmp.skilltestphndr.ui.partners.form.data

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import mx.com.maiktmp.database.dao.PartnerDao
import mx.com.maiktmp.database.entities.PartnerDB
import mx.com.maiktmp.skilltestphndr.ui.models.GenericResponse

class PartnerFormRepository(private val dao: PartnerDao) {

    fun storeDbPartner(partnerDB: PartnerDB, cb: (GenericResponse<Any?>) -> Unit): Disposable {
        return dao.upsert(partnerDB)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { cb(GenericResponse(success = true)) },
                { cb(GenericResponse()) }
            )
    }

}