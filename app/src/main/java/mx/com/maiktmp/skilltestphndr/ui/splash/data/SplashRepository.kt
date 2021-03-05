package mx.com.maiktmp.skilltestphndr.ui.splash.data

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashRepository() {

    private val auth = Firebase.auth

    fun checkUser(cbResult: (FirebaseUser?) -> Unit) {
        cbResult(auth.currentUser)
    }

}