package mx.com.maiktmp.skilltestphndr.utils.files

import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

object FileUtils {

    fun loadJSONFromAsset(file: File): String? {
        var json: String? = null
        json = try {
            val `is`: InputStream = FileInputStream(file)
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}