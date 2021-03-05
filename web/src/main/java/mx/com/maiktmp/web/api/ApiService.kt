package mx.com.maiktmp.web.api


import mx.com.maiktmp.web.ApiServer
import mx.com.maiktmp.web.api.models.FileAPI
import mx.com.maiktmp.web.api.models.GenericResponseAPI
import retrofit2.Call
import retrofit2.http.GET


interface ApiService {

    @GET("${ApiServer.name}/s/5u21281sca8gj94/getFile.json?dl=0")
    fun getPartnersRepository(): Call<GenericResponseAPI<FileAPI>>

}