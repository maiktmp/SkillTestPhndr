package mx.com.maiktmp.skilltestphndr.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import mx.com.maiktmp.skilltestphndr.R


object Extensions {


    //Toast
    fun Context?.displayToast(message: String?, duration: Int) {
        Toast.makeText(this, message, duration).show()
    }


    //    Visibility
    fun View.showView() {
        this.visibility = View.VISIBLE
    }

    fun View.hideView() {
        this.visibility = View.GONE
    }


    //Validations
    fun TextInputLayout.required(): Boolean {
        this.error = null;
        val text = this.editText?.text.toString()
        if (text == "") {
            this.error = context.getString(R.string.validation_require)
            return false
        }
        return true
    }

    fun TextInputLayout.validateMail(): Boolean {
        this.error = null
        val text = this.editText?.text.toString()
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            this.error = context.getString(R.string.validation_email)
            return false
        }
        return true
    }


    //Model conversion


    fun <T> Any.convert(cl: Class<T>): T {
        val gson = Gson()
        val data = gson.toJsonTree(this).asJsonObject
        return gson.fromJson(data, cl)
    }

    fun <T> List<Any>.convertToList(model: Class<T>): List<T> = this.map { it.convert(model) }

}