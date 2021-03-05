package mx.com.maiktmp.web.api

import android.util.Log
import com.google.gson.GsonBuilder
import mx.com.maiktmp.web.ApiServer
import mx.com.maiktmp.web.api.models.FileAPI
import mx.com.maiktmp.web.api.models.GenericResponseAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


object ApiConnection {

    private lateinit var service: ApiService

    init {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)

        val gson = GsonBuilder()
            .create()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(ApiServer.name)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClientBuilder.build())
            .build()

        service = retrofit.create(ApiService::class.java)

    }


    private fun <T> enqueueCall(call: Call<T>, cbResult: (status: Boolean, result: T?) -> Unit) {
        call.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                Log.e(this::class.java.name, "Error Response: " + t.message)
                cbResult(false, null)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    cbResult(true, response.body()!!)
                } else {
                    cbResult(false, null)
                }
            }
        })
    }


    fun getEmployees(cbResult: (status: Boolean, result: GenericResponseAPI<FileAPI>?) -> Unit) {
        enqueueCall(service.getPartnersRepository(), cbResult)
    }


}