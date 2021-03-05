package mx.com.maiktmp.skilltestphndr.ui.home.presenter

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import mx.com.maiktmp.database.entities.PartnerDB
import mx.com.maiktmp.skilltestphndr.R
import mx.com.maiktmp.skilltestphndr.base.BackupBase
import mx.com.maiktmp.skilltestphndr.base.BasePresenter
import mx.com.maiktmp.skilltestphndr.ui.home.data.HomeRepository
import mx.com.maiktmp.skilltestphndr.ui.home.view.interfaces.HomeView
import mx.com.maiktmp.skilltestphndr.utils.NetworkUtil
import mx.com.maiktmp.skilltestphndr.utils.files.DownloadFileAsync
import mx.com.maiktmp.skilltestphndr.utils.files.FileCompressor
import mx.com.maiktmp.skilltestphndr.utils.files.FileUtils
import mx.com.maiktmp.web.api.ApiConnection
import java.io.File
import java.util.concurrent.RecursiveTask

class HomePresenter(
    private val context: Context,
    private val repostory: HomeRepository,
    private val backupBase: BackupBase,
    val api: ApiConnection
) : BasePresenter<HomeView>() {

    private val tmpFiles = ArrayList<File>()

    fun attemptDownloadData() {
        view()?.showProgressDialog()
        //Check local
        repostory.getLocalPartners {
            if (!it.success) {
                view()?.hideProgressDialog()
                view()?.showError(context.getString(R.string.error_local_storage))
                return@getLocalPartners
            }
            // Count local partners
            if (it.data == 0) {
                downloadData()
            } else {
                view()?.hideProgressDialog()
                view()?.handleDataSuccess()
                backupBase.attemptBackup()
            }
        }
    }

    private fun downloadData() {
        //Check network
        if (!NetworkUtil.isNetworkAvailable(context)) {
            view()?.hideProgressDialog()
            view()?.showError(context.getString(R.string.error_network))
            return
        }

        //Download data

        //Get Employees Api
        api.getEmployees { status, result ->
            if (!status) {
                displayError(context.getString(R.string.error_download_partners))
                return@getEmployees
            }
            if (result?.data?.file == null) {
                displayError(context.getString(R.string.error_download_partners))
                return@getEmployees
            }

            //Download zip file from url
            downloadFileAsync(result.data?.file!!) { fileDownloaded ->
                if (fileDownloaded == null) {
                    displayError(context.getString(R.string.error_download_partners))
                    return@downloadFileAsync
                }

                //Decompress Zip
                tmpFiles.add(fileDownloaded)
                decompressFile(fileDownloaded) { filesDecompress ->
                    if (filesDecompress == null) {
                        displayError(context.getString(R.string.error_decompress_partners))
                        return@decompressFile
                    }

                    tmpFiles.addAll(filesDecompress)
                    //Sore JsonFile
                    val jsonFileString = FileUtils.loadJSONFromAsset(filesDecompress.first())
                    val objectResult = Gson().fromJson(jsonFileString, JsonObject::class.java)
                    storePartnersGson(objectResult["data"].asJsonObject["employees"])
                }
            }
        }
    }


    private fun downloadFileAsync(fileUrl: String, cb: (File?) -> Unit) {
        DownloadFileAsync(
            fileUrl,
            "tmp_partners.zip",
            context
        ).attemptDownload {
            cb(it)
        }
    }


    private fun decompressFile(file: File, cb: (List<File>?) -> Unit) {
        val files = FileCompressor(context, file).unzip()
        cb(files)
    }


    private fun storePartnersGson(partners: JsonElement) {
        val gson = Gson()
        val res = partners.asJsonArray.map { gson.fromJson(it, PartnerDB::class.java) }
        repostory.storePartner(*res.toTypedArray()) {
            if (!it.success) {
                displayError(context.getString(R.string.error_decompress_partners))
                return@storePartner
            }
            view()?.hideProgressDialog()
            view()?.handleDataSuccess()
            clearTmpFiles()
            backupBase.attemptBackup()
        }.autoClear()
    }

    private fun displayError(message: String) {
        view()?.hideProgressDialog()
        view()?.showError(message)
        clearTmpFiles()
    }

    private fun clearTmpFiles() {
        for (tmpFile in tmpFiles) {
            tmpFile.delete()
        }
    }

    fun logout() {
        repostory.logoutFromFb()
        view()?.logout()
    }

}