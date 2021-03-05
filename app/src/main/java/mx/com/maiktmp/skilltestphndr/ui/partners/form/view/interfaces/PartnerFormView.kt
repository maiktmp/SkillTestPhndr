package mx.com.maiktmp.skilltestphndr.ui.partners.form.view.interfaces

interface PartnerFormView {

    fun handleStoreSuccess()

    fun handleStoreError(message: String)

    fun showProgress()

    fun hideProgress()
}