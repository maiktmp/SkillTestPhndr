package mx.com.maiktmp.skilltestphndr.ui.partners.map.presenter

import android.content.Context
import mx.com.maiktmp.skilltestphndr.R
import mx.com.maiktmp.skilltestphndr.base.BasePresenter
import mx.com.maiktmp.skilltestphndr.ui.models.Partner
import mx.com.maiktmp.skilltestphndr.ui.partners.list.view.interfaces.PartnersView
import mx.com.maiktmp.skilltestphndr.ui.partners.map.data.PartnersMapRepository
import mx.com.maiktmp.skilltestphndr.ui.partners.map.view.interfaces.PartnersMapView
import mx.com.maiktmp.skilltestphndr.utils.Extensions.convertToList

class PartnersMapPresenter(val context: Context, private val repository: PartnersMapRepository) :
    BasePresenter<PartnersMapView>() {

    fun getPartners() {
        view()?.showProgress()
        repository.getDatabasePartners {
            view()?.hideProgress()
            if (!it.success) {
                view()?.handleUnsuccessful(context.getString(R.string.error_get_local_partners))
                return@getDatabasePartners
            }

            view()?.showMarkers(it.data!!.convertToList(Partner::class.java))
        }
    }

}