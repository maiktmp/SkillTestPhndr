package mx.com.maiktmp.skilltestphndr.ui.home.view.interfaces

interface HomeView {

    fun showProgressDialog()

    fun hideProgressDialog()

    fun showError(message: String)

    fun handleDataSuccess()


}