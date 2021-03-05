package mx.com.maiktmp.skilltestphndr.ui.partners.map.data

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import mx.com.maiktmp.database.dao.PartnerDao
import mx.com.maiktmp.database.entities.PartnerDB
import mx.com.maiktmp.skilltestphndr.ui.models.GenericResponse

class PartnersMapRepository(private val partnersDao: PartnerDao) {

    fun getDatabasePartners(cb: (GenericResponse<List<PartnerDB>?>) -> Unit): Disposable? {
        return partnersDao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { cb(GenericResponse(success = true, data = it)) },
                { cb(GenericResponse(data = null)) })
    }

}