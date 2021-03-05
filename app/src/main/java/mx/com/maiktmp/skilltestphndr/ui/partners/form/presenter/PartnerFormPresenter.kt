package mx.com.maiktmp.skilltestphndr.ui.partners.form.presenter

import android.content.Context
import mx.com.maiktmp.database.entities.PartnerDB
import mx.com.maiktmp.skilltestphndr.R
import mx.com.maiktmp.skilltestphndr.base.BasePresenter
import mx.com.maiktmp.skilltestphndr.ui.models.Partner
import mx.com.maiktmp.skilltestphndr.ui.partners.form.data.PartnerFormRepository
import mx.com.maiktmp.skilltestphndr.ui.partners.form.view.interfaces.PartnerFormView
import mx.com.maiktmp.skilltestphndr.utils.Extensions.convert

class PartnerFormPresenter(
    private val context: Context,
    private val repository: PartnerFormRepository
) : BasePresenter<PartnerFormView>() {

    fun storePartner(partner: Partner) {
        val partnerDb = partner.convert(PartnerDB::class.java)
        view()?.showProgress()
        repository.storeDbPartner(partnerDb) {
            view()?.hideProgress()
            if (!it.success) {
                view()?.handleStoreError(context.getString(R.string.error_save_local_partners))
                return@storeDbPartner
            }

            view()?.handleStoreSuccess()
        }
    }

}