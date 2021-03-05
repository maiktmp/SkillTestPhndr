package mx.com.maiktmp.skilltestphndr.ui.auth.view.interfaces

import mx.com.maiktmp.skilltestphndr.ui.models.GenericResponse
import mx.com.maiktmp.skilltestphndr.ui.models.User

interface LoginView {

    fun handleLoginSuccess()

    fun handleLoginError(gr: GenericResponse<User?>)

    fun showProgress()

    fun hideProgress()

}