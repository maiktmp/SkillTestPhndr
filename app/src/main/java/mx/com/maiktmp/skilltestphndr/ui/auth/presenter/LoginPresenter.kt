package mx.com.maiktmp.skilltestphndr.ui.auth.presenter

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import mx.com.maiktmp.skilltestphndr.R
import mx.com.maiktmp.skilltestphndr.base.BasePresenter
import mx.com.maiktmp.skilltestphndr.ui.auth.data.FBRepository
import mx.com.maiktmp.skilltestphndr.ui.auth.view.interfaces.LoginView
import mx.com.maiktmp.skilltestphndr.ui.models.GenericResponse
import mx.com.maiktmp.skilltestphndr.ui.models.User

class LoginPresenter(
    private val context: Context,
    private val fbRepository: FBRepository,
) : BasePresenter<LoginView>() {

    fun attemptLogin(user: User) {
        view()?.showProgress()
        fbRepository.attemptLoginFb(user) {
            processLoginResult(it)
        }
    }


    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account?.idToken!!, null)
            fbRepository.attemptWithCredentials(credential) {
                processLoginResult(it)
            }
        } catch (e: ApiException) {
            view()?.handleLoginError(GenericResponse(message = context.getString(R.string.error_incomplete_login)))
        }
    }

    private fun processLoginResult(gr: GenericResponse<User?>) {
        view()?.hideProgress()
        if (!gr.success) {
            view()?.handleLoginError(gr)
            return
        }
        view()?.handleLoginSuccess()
    }


}