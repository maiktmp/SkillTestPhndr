package mx.com.maiktmp.skilltestphndr.ui.home.data

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import mx.com.maiktmp.database.dao.PartnerDao
import mx.com.maiktmp.database.entities.PartnerDB
import mx.com.maiktmp.skilltestphndr.ui.models.GenericResponse

class HomeRepository(private val partnerDao: PartnerDao) {

    fun getLocalPartners(cbResult: (GenericResponse<Int?>) -> Unit): Disposable {
        return partnerDao.count()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { cbResult(GenericResponse(success = true, data = it)) },
                { cbResult(GenericResponse(data = null)) }
            )
    }

    fun storePartner(vararg partners: PartnerDB, cbResult: (GenericResponse<Int?>) -> Unit): Disposable {
        return partnerDao.upsert(*partners)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { cbResult(GenericResponse(success = true)) },
                { cbResult(GenericResponse(data = null)) }
            )
    }

}