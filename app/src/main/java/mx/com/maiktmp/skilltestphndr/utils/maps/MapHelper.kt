package mx.com.maiktmp.skilltestphndr.utils.maps

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import mx.com.maiktmp.skilltestphndr.R
import mx.com.maiktmp.skilltestphndr.databinding.ItemInfowindowBinding
import mx.com.maiktmp.skilltestphndr.ui.models.Partner


class MapHelper(
    private val activity: Activity,
    private val mapHandler: MapHandler
) : OnMapReadyCallback {


    lateinit var gMap: GoogleMap

    override fun onMapReady(gMap: GoogleMap?) {
        this.gMap = gMap!!
        mapHandler.onMapCompletelyConfigured(gMap)
        setupMapUi()
    }

    fun displayMarkers(
        location: LatLng,
        color: Float = BitmapDescriptorFactory.HUE_RED,
        tag: Any? = null
    ) {
        gMap.addMarker(
            MarkerOptions()
                .position(location)
                .icon(BitmapDescriptorFactory.defaultMarker(color))
        ).tag = tag
    }

    fun centerCameraIn(vararg positions: LatLng) {
        val builder = LatLngBounds.Builder()
        for (position in positions) {
            builder.include(position)
        }
        val bounds = builder.build()
        val padding = 300
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        gMap.animateCamera(cu)
    }

    fun centerCameraIn(position: LatLng) {
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15f))
    }

    private fun setupMapUi() {
        gMap.uiSettings.isZoomControlsEnabled = true
        gMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(p0: Marker?): View? {
                return null
            }

            @SuppressLint("SetTextI18n")
            override fun getInfoContents(marker: Marker?): View? {
                return if (marker?.tag == null) {
                    null
                } else {
                    val v =
                        LayoutInflater.from(activity).inflate(R.layout.item_infowindow, null, false)
                    val vBind = ItemInfowindowBinding.bind(v)
                    val partner = Gson().fromJson(marker.tag.toString(), Partner::class.java)
                    vBind.tvEmail.text = "Email: ${partner.mail}"
                    vBind.tvName.text = "Nombre: ${partner.name}"
                    v
                }
            }

        })
    }

    interface MapHandler {
        fun onMapCompletelyConfigured(gMap: GoogleMap?)
    }
}