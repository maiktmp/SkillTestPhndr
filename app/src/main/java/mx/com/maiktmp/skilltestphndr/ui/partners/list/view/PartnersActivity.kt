package mx.com.maiktmp.skilltestphndr.ui.partners.list.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import mx.com.maiktmp.database.DBSkillTestPhndr
import mx.com.maiktmp.skilltestphndr.R
import mx.com.maiktmp.skilltestphndr.databinding.ActivityPartnersBinding
import mx.com.maiktmp.skilltestphndr.ui.CustomActivity
import mx.com.maiktmp.skilltestphndr.ui.models.Partner
import mx.com.maiktmp.skilltestphndr.ui.partners.list.data.PartnersRepository
import mx.com.maiktmp.skilltestphndr.ui.partners.list.presenter.PartnerPresenter
import mx.com.maiktmp.skilltestphndr.ui.partners.list.view.adapter.PartnerAdapter
import mx.com.maiktmp.skilltestphndr.ui.partners.list.view.interfaces.PartnersView
import mx.com.maiktmp.skilltestphndr.ui.partners.map.view.PartnersMapActivity
import mx.com.maiktmp.skilltestphndr.utils.Extensions.displayToast
import mx.com.maiktmp.skilltestphndr.utils.Extensions.hideView
import mx.com.maiktmp.skilltestphndr.utils.Extensions.showView

class PartnersActivity : CustomActivity(), PartnersView {

    private val vBind: ActivityPartnersBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_partners)
    }

    private val presenter: PartnerPresenter by lazy {
        PartnerPresenter(this, PartnersRepository(DBSkillTestPhndr.db.partnerDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vBind.iToolbar
        presenter.attachView(this, lifecycle)
        presenter.getPartners()
        super.setupBackButton(vBind.iToolbar, getString(R.string.partners_title))
    }

    private fun setupRecyclerView() {
        vBind.rvPartners.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
        }
    }

    private fun setupAdapter(partners: List<Partner>) {
        val adapter = PartnerAdapter(partners)
        adapter.onPartnerClick = {
            val i = Intent(this, PartnersMapActivity::class.java)
            i.putExtra(PartnersMapActivity.PARTNER_ID, it.idLocal)
            startActivity(i)
        }
        vBind.rvPartners.adapter = adapter
    }


    override fun listPartners(partners: List<Partner>) {
        if (partners.isEmpty()) {
            vBind.rvPartners.hideView()
            vBind.lblEmpty.showView()
            return
        }
        setupRecyclerView()
        setupAdapter(partners)
    }

    override fun handleUnsuccessful(message: String?) {
        message?.let {
            displayToast(message, Toast.LENGTH_SHORT)
        }
    }

    override fun showProgress() {
        vBind.pbLoading.showView()
    }

    override fun hideProgress() {
        vBind.pbLoading.hideView()
    }


}