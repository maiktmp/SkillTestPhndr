package mx.com.maiktmp.skilltestphndr.ui.partners.list.view.interfaces

import mx.com.maiktmp.skilltestphndr.ui.models.Partner

interface PartnersView {

    fun listPartners(partners: List<Partner>)

    fun handleUnsuccessful(message: String?)

    fun showProgress()

    fun hideProgress()

}