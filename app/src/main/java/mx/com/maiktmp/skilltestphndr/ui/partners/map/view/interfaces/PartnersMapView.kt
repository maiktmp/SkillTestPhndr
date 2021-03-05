package mx.com.maiktmp.skilltestphndr.ui.partners.map.view.interfaces

import mx.com.maiktmp.skilltestphndr.ui.models.Partner

interface PartnersMapView {

    fun showMarkers(partners: List<Partner>)

    fun handleUnsuccessful(message: String?)

    fun showProgress()

    fun hideProgress()


}