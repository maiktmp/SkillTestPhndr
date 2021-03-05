package mx.com.maiktmp.skilltestphndr.ui.home.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.kaopiz.kprogresshud.KProgressHUD
import mx.com.maiktmp.database.DBSkillTestPhndr
import mx.com.maiktmp.skilltestphndr.R
import mx.com.maiktmp.skilltestphndr.base.BackupBase
import mx.com.maiktmp.skilltestphndr.databinding.ActivityHomeBinding
import mx.com.maiktmp.skilltestphndr.ui.CustomActivity
import mx.com.maiktmp.skilltestphndr.ui.auth.view.LoginActivity
import mx.com.maiktmp.skilltestphndr.ui.home.data.HomeRepository
import mx.com.maiktmp.skilltestphndr.ui.home.presenter.HomePresenter
import mx.com.maiktmp.skilltestphndr.ui.home.view.interfaces.HomeView
import mx.com.maiktmp.skilltestphndr.ui.partners.form.view.PartnerFormActivity
import mx.com.maiktmp.skilltestphndr.ui.partners.list.view.PartnersActivity
import mx.com.maiktmp.skilltestphndr.utils.Codes
import mx.com.maiktmp.skilltestphndr.utils.Extensions.displayToast
import mx.com.maiktmp.web.api.ApiConnection

class HomeActivity : CustomActivity(), HomeView {

    private val vBind: ActivityHomeBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_home)
    }

    private val homePresenter by lazy {
        HomePresenter(
            this,
            HomeRepository(DBSkillTestPhndr.db.partnerDao()),
            BackupBase(DBSkillTestPhndr.db.partnerDao()),
            ApiConnection
        )
    }

    private var dialog: KProgressHUD? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == Codes.REQUEST_CREATE_PARTNER) {
            displayToast(getString(R.string.create_partner_success), Toast.LENGTH_SHORT)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vBind.cvAddPartners
        homePresenter.attachView(this, lifecycle)
        homePresenter.attemptDownloadData()

        super.setupToolbar(vBind.iToolbar.toolbar)
        super.setToolbarTitle(vBind.iToolbar, getString(R.string.home))
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_logout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.log_out -> {
                displayLogoutDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun displayLogoutDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.log_out))
            .setMessage(getString(R.string.log_out_ad))
            .setNegativeButton(getString(R.string.ok)) { _, _ ->
                homePresenter.logout()
            }
            .setPositiveButton(getString(R.string.cancel)) { _, _ ->
            }
            .show()
    }

    private fun setupListPartners() {
        vBind.cvPartners.setOnClickListener {
            startActivity(Intent(this, PartnersActivity::class.java))
        }
    }

    private fun setupAddPartner() {
        vBind.cvAddPartners.setOnClickListener {
            startActivityForResult(
                Intent(this, PartnerFormActivity::class.java),
                Codes.REQUEST_CREATE_PARTNER
            )
        }
    }


    override fun showProgressDialog() {
        dialog = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel(getString(R.string.loading))
            .setCancellable(false)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .show()
    }

    override fun hideProgressDialog() {
        dialog?.dismiss()
    }

    override fun showError(message: String) {
        Snackbar.make(vBind.rootElement, message, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.retry) { homePresenter.attemptDownloadData() }
            .show()
    }

    override fun handleDataSuccess() {
        activateCard(vBind.cvAddPartners, vBind.ivAddPartners, vBind.lblAddPartners)
        activateCard(vBind.cvPartners, vBind.ivPartners, vBind.lblPartners)

        setupListPartners()
        setupAddPartner()
    }

    override fun logout() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }


    private fun activateCard(card: MaterialCardView, image: ImageView, text: TextView) {
        card.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.primaryLightColor)

        image.imageTintList =
            ContextCompat.getColorStateList(this, R.color.white)

        text.setTextColor(ContextCompat.getColor(this, R.color.white))
    }

}