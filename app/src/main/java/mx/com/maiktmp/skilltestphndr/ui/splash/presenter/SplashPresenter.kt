package mx.com.maiktmp.skilltestphndr.ui.splash.presenter

import mx.com.maiktmp.skilltestphndr.base.BasePresenter
import mx.com.maiktmp.skilltestphndr.ui.splash.data.SplashRepository
import mx.com.maiktmp.skilltestphndr.ui.splash.view.interfaces.SplashView

class SplashPresenter(private val splashRepository: SplashRepository) :
    BasePresenter<SplashView>() {

    fun checkSession() {
        splashRepository.checkUser {
            view()?.resultSession(it)
        }
    }

}