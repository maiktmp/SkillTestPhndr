package mx.com.maiktmp.skilltestphndr.ui.splash.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseUser
import mx.com.maiktmp.skilltestphndr.R
import mx.com.maiktmp.skilltestphndr.databinding.ActivitySplashBinding
import mx.com.maiktmp.skilltestphndr.ui.auth.view.LoginActivity
import mx.com.maiktmp.skilltestphndr.ui.home.view.HomeActivity
import mx.com.maiktmp.skilltestphndr.ui.splash.data.SplashRepository
import mx.com.maiktmp.skilltestphndr.ui.splash.presenter.SplashPresenter
import mx.com.maiktmp.skilltestphndr.ui.splash.view.interfaces.SplashView

class SplashActivity : AppCompatActivity(), SplashView {

    private val vBind: ActivitySplashBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_splash)
    }

    private val presenter: SplashPresenter by lazy {
        SplashPresenter(SplashRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this, lifecycle)
        vBind.lblScreen
        presenter.checkSession()
    }

    override fun resultSession(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, HomeActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}