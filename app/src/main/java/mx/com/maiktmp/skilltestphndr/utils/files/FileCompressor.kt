package mx.com.maiktmp.skilltestphndr.utils.files

import android.content.Context
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class FileCompressor(
    private val context: Context,
    private val file: File
) {

    fun unzip(): List<File>? {
        val results = ArrayList<File>()
        try {
            val fin = FileInputStream(file)
            val zin = ZipInputStream(fin)
            var ze: ZipEntry? = null
            while (zin.nextEntry.also { ze = it } != null) {

                File.createTempFile(ze!!.name, null, context.cacheDir)
                val file = File(context.cacheDir, ze!!.name)

                val fout = FileOutputStream(file)
                val baos = ByteArrayOutputStream()
                val buffer = ByteArray(1024)
                var count: Int
                while (zin.read(buffer).also { count = it } != -1) {
                    baos.write(buffer, 0, count)
                    val bytes: ByteArray = baos.toByteArray()
                    fout.write(bytes)
                    baos.reset()
                }
                fout.close()
                zin.closeEntry()
                results.add(file)
            }
            zin.close()
        } catch (e: Exception) {
            Log.e(this::class.simpleName, e.stackTraceToString(), e)
            return null
        }
        return results
    }

}