package mx.com.maiktmp.skilltestphndr.ui.partners.list.presenter

import android.content.Context
import mx.com.maiktmp.skilltestphndr.R
import mx.com.maiktmp.skilltestphndr.base.BasePresenter
import mx.com.maiktmp.skilltestphndr.ui.models.Partner
import mx.com.maiktmp.skilltestphndr.ui.partners.list.data.PartnersRepository
import mx.com.maiktmp.skilltestphndr.ui.partners.list.view.interfaces.PartnersView
import mx.com.maiktmp.skilltestphndr.utils.Extensions.convertToList

class PartnerPresenter(val context: Context, private val repository: PartnersRepository) :
    BasePresenter<PartnersView>() {

    fun getPartners() {
        view()?.showProgress()
        repository.getDatabasePartners {
            view()?.hideProgress()
            if (!it.success) {
                view()?.handleUnsuccessful(context.getString(R.string.error_get_local_partners))
                return@getDatabasePartners
            }

            view()?.listPartners(it.data!!.convertToList(Partner::class.java))
        }
    }

}