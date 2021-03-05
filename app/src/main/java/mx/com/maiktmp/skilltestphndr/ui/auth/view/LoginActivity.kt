package mx.com.maiktmp.skilltestphndr.ui.auth.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import mx.com.maiktmp.skilltestphndr.R
import mx.com.maiktmp.skilltestphndr.databinding.ActivityLoginBinding
import mx.com.maiktmp.skilltestphndr.ui.auth.data.FBRepository
import mx.com.maiktmp.skilltestphndr.ui.auth.presenter.LoginPresenter
import mx.com.maiktmp.skilltestphndr.ui.auth.view.interfaces.LoginView
import mx.com.maiktmp.skilltestphndr.ui.home.view.HomeActivity
import mx.com.maiktmp.skilltestphndr.ui.models.GenericResponse
import mx.com.maiktmp.skilltestphndr.ui.models.User
import mx.com.maiktmp.skilltestphndr.utils.Codes
import mx.com.maiktmp.skilltestphndr.utils.Extensions.displayToast
import mx.com.maiktmp.skilltestphndr.utils.Extensions.required
import mx.com.maiktmp.skilltestphndr.utils.Extensions.validateMail

class LoginActivity : AppCompatActivity(), LoginView {

    private val vBind: ActivityLoginBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_login)
    }

    private val loginPresenter by lazy {
        LoginPresenter(this, FBRepository(this))
    }

    val user = User()


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Codes.REQUEST_GOOGLE_SIG_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            loginPresenter.handleSignInResult(task)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginPresenter.attachView(this, lifecycle)
        vBind.user = user
        setupSigInButton()
        setupSigInGoogle()
    }


    private fun setupSigInButton() {
        bindProgressButton(vBind.btnLogin)
        vBind.btnLogin.attachTextChangeAnimator()
        vBind.btnLogin.setOnClickListener {
            if (validate()) {
                loginPresenter.attemptLogin(user)
            }
        }
    }

    private fun setupSigInGoogle() {
        vBind.btnLoginGoogle.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(this, gso)
            val signInIntent: Intent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, Codes.REQUEST_GOOGLE_SIG_IN)
        }
    }


    private fun validate(): Boolean {
        return vBind.tilEmail.required() &&
                vBind.tilEmail.validateMail() &&
                vBind.tilPassword.required()

    }

    override fun handleLoginSuccess() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    override fun handleLoginError(gr: GenericResponse<User?>) {
        displayToast(gr.message, Toast.LENGTH_SHORT)
    }

    override fun showProgress() {
        vBind.btnLogin.showProgress {
            buttonTextRes = R.string.loading
            progressColor = Color.WHITE
        }
    }

    override fun hideProgress() {
        vBind.btnLogin.hideProgress(R.string.login)
    }


}