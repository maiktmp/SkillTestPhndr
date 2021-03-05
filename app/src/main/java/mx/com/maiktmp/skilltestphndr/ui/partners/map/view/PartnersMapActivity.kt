package mx.com.maiktmp.skilltestphndr.ui.partners.map.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.kaopiz.kprogresshud.KProgressHUD
import mx.com.maiktmp.database.DBSkillTestPhndr
import mx.com.maiktmp.skilltestphndr.R
import mx.com.maiktmp.skilltestphndr.databinding.ActivityPartnersMapBinding
import mx.com.maiktmp.skilltestphndr.ui.CustomActivity
import mx.com.maiktmp.skilltestphndr.ui.models.Partner
import mx.com.maiktmp.skilltestphndr.ui.partners.map.data.PartnersMapRepository
import mx.com.maiktmp.skilltestphndr.ui.partners.map.presenter.PartnersMapPresenter
import mx.com.maiktmp.skilltestphndr.ui.partners.map.view.interfaces.PartnersMapView
import mx.com.maiktmp.skilltestphndr.utils.Extensions.displayToast
import mx.com.maiktmp.skilltestphndr.utils.maps.MapHelper

class PartnersMapActivity : CustomActivity(), MapHelper.MapHandler, PartnersMapView {

    companion object {
        const val PARTNER_ID = "PARTNER_ID"
    }

    private val vBind: ActivityPartnersMapBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_partners_map)
    }

    private val mapHelper: MapHelper by lazy {
        MapHelper(this, this)
    }

    private val partnerMapPresenter: PartnersMapPresenter by lazy {
        PartnersMapPresenter(this, PartnersMapRepository(DBSkillTestPhndr.db.partnerDao()))
    }

    private var dialog: KProgressHUD? = null

    lateinit var gMap: GoogleMap

    private var partnerId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retrieveExtras()
        vBind.iToolbar
        partnerMapPresenter.attachView(this, lifecycle)
        setupMap()
        super.setupBackButton(vBind.iToolbar, getString(R.string.partner_places_title))
    }


    private fun retrieveExtras() {
        partnerId = intent.getLongExtra(PARTNER_ID, 0)
    }

    private fun setupMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.f_map) as SupportMapFragment?
        mapFragment!!.getMapAsync(mapHelper)
    }


    override fun onMapCompletelyConfigured(gMap: GoogleMap?) {
        this.gMap = gMap!!
        partnerMapPresenter.getPartners()
    }

    override fun showMarkers(partners: List<Partner>) {
        val gson = Gson()
        val locations = partners.map { LatLng(it.location?.lat!!, it.location?.log!!) }
        mapHelper.centerCameraIn(*locations.toTypedArray())

        for (partner in partners) {
            val color = if (partner.idLocal == partnerId) BitmapDescriptorFactory.HUE_GREEN
            else BitmapDescriptorFactory.HUE_RED

            println(partner)

            mapHelper.displayMarkers(
                LatLng(partner.location?.lat!!, partner.location?.log!!),
                color,
                gson.toJson(partner).toString()
            )
        }
    }

    override fun handleUnsuccessful(message: String?) {
        message?.let {
            displayToast(message, Toast.LENGTH_SHORT)
        }
    }

    override fun showProgress() {
        dialog = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel(getString(R.string.loading))
            .setCancellable(false)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .show()
    }

    override fun hideProgress() {
        dialog?.dismiss()
    }


}