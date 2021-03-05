package mx.com.maiktmp.skilltestphndr.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import mx.com.maiktmp.skilltestphndr.R
import mx.com.maiktmp.skilltestphndr.databinding.ToolbarBinding

abstract class CustomActivity : AppCompatActivity() {


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun setupBackButton(vToolbar: ToolbarBinding, title: String? = null) {
        setSupportActionBar(vToolbar.toolbar)
        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_24)
        val actionBar = this.supportActionBar
        actionBar?.setHomeAsUpIndicator(drawable)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setDisplayShowTitleEnabled(false)
        setToolbarTitle(vToolbar, title)
    }

    fun setToolbarTitle(vToolbar: ToolbarBinding, title: String? = null) {
        vToolbar.tvTitle.text = title
    }


}