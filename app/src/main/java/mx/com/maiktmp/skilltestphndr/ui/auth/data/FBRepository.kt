package mx.com.maiktmp.skilltestphndr.ui.auth.data

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import mx.com.maiktmp.skilltestphndr.R
import mx.com.maiktmp.skilltestphndr.ui.models.GenericResponse
import mx.com.maiktmp.skilltestphndr.ui.models.User


class FBRepository(private val context: Context) {

    private val auth = Firebase.auth

    fun attemptLoginFb(user: User, cb: (result: GenericResponse<User?>) -> Unit) {
        auth.signInWithEmailAndPassword(user.email!!, user.password!!)
            .addOnCompleteListener { processFbTask(it, cb) }
    }


    fun attemptWithCredentials(
        credential: AuthCredential,
        cb: (result: GenericResponse<User?>) -> Unit
    ) {
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener { processFbTask(it, cb) }
    }

    private fun processFbTask(
        task: Task<AuthResult>,
        cb: (result: GenericResponse<User?>) -> Unit
    ) {
        val gr = GenericResponse<User?>()
        if (!task.isSuccessful) {
            gr.apply {
                message = context.getString(R.string.error_credentials)
            }
        } else {
//                    val user = auth.currentUser
            gr.apply {
                success = true
            }
        }
        cb(gr)
    }
}