package mx.com.maiktmp.skilltestphndr.ui.partners.form.view

import android.graphics.Color

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import mx.com.maiktmp.database.DBSkillTestPhndr
import mx.com.maiktmp.skilltestphndr.R
import mx.com.maiktmp.skilltestphndr.base.BackupBase
import mx.com.maiktmp.skilltestphndr.databinding.ActivityPartnerFormBinding
import mx.com.maiktmp.skilltestphndr.ui.CustomActivity
import mx.com.maiktmp.skilltestphndr.ui.models.Location
import mx.com.maiktmp.skilltestphndr.ui.models.Partner
import mx.com.maiktmp.skilltestphndr.ui.partners.form.data.PartnerFormRepository
import mx.com.maiktmp.skilltestphndr.ui.partners.form.presenter.PartnerFormPresenter
import mx.com.maiktmp.skilltestphndr.ui.partners.form.view.interfaces.PartnerFormView
import mx.com.maiktmp.skilltestphndr.utils.Extensions.displayToast
import mx.com.maiktmp.skilltestphndr.utils.Extensions.required
import mx.com.maiktmp.skilltestphndr.utils.Extensions.validateMail
import mx.com.maiktmp.skilltestphndr.utils.maps.MapHelper
import java.util.concurrent.ThreadLocalRandom

class PartnerFormActivity : CustomActivity(), PartnerFormView, MapHelper.MapHandler {

    private val vBind: ActivityPartnerFormBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_partner_form)
    }

    private val presenter: PartnerFormPresenter by lazy {
        PartnerFormPresenter(this, PartnerFormRepository(DBSkillTestPhndr.db.partnerDao()))
    }

    private val mapHelper: MapHelper by lazy {
        MapHelper(this, this)
    }

    lateinit var gMap: GoogleMap

    private val partner = Partner()
    private val location = Location()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this, lifecycle)
        vBind.partner = partner
        setupSaveBtn()
        setupMap()
        super.setupBackButton(vBind.iToolbar, getString(R.string.add_partner_title))
    }


    private fun setupMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.f_map) as SupportMapFragment?
        mapFragment!!.getMapAsync(mapHelper)
    }

    private fun setupLocation() {
        //19.297844, -99.204495
        //19.307117, -99.205243

        val lat = ThreadLocalRandom.current()
            .nextDouble(
                19.2978454,
                19.307107
            )

        val lng = ThreadLocalRandom.current().nextDouble(
            -99.205253,
            -99.204485
        )

        val latLng = LatLng(lat, lng)

        mapHelper.displayMarkers(latLng)
        mapHelper.centerCameraIn(latLng)

        location.lat = lat
        location.log = lng
    }

    private fun setupSaveBtn() {
        vBind.btnSave.setOnClickListener {
            if (validate()) {
                partner.location = location
                presenter.storePartner(partner)
            }
        }
    }

    private fun validate(): Boolean {
        return vBind.tilEmail.required() &&
                vBind.tilEmail.validateMail() &&
                vBind.tilName.required()
    }

    override fun handleStoreSuccess() {
        BackupBase(DBSkillTestPhndr.db.partnerDao()).attemptBackup()
        setResult(RESULT_OK)
        finish()
    }

    override fun handleStoreError(message: String) {
        displayToast(message, Toast.LENGTH_SHORT)
    }

    override fun showProgress() {
        vBind.btnSave.showProgress {
            buttonTextRes = R.string.loading
            progressColor = Color.WHITE
        }
    }

    override fun hideProgress() {
        vBind.btnSave.hideProgress(R.string.save)
    }

    override fun onMapCompletelyConfigured(gMap: GoogleMap?) {
        this.gMap = gMap!!
        setupLocation()
    }


}