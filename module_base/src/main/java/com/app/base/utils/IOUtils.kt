package com.app.base.utils

import android.annotation.SuppressLint
import android.os.Environment
import com.app.base.AppContext
import timber.log.Timber
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

object IOUtils {

    fun File.copyInputStreamToFile(inputStream: InputStream) {
        inputStream.use { input ->
            this.outputStream().use { fileOut ->
                input.copyTo(fileOut)
            }
        }
    }

    /**
     * 20200828_1598606727760.suffix
     *
     * @param suffix mp3/mp4
     */
    @SuppressLint("SimpleDateFormat")
    fun createFilePrefix(suffix: String): String {
        val date = Date()
        val format = SimpleDateFormat("yyyyMMdd")
        val fileName = "${format.format(date)}_${System.currentTimeMillis()}.$suffix"
        Timber.i("fileName: $fileName")
        return fileName
    }

    /**
     * /storage/emulated/0 内部存储目录
     *
     * /storage/emulated/0/Android/data/com.company.dm/files/20200828_1598606727760.mp3
     *
     * @param suffix mp3/mp4
     */
    fun createFileToDataDataDir(suffix: String): File {
        return File(
            "${
                AppContext.get().getExternalFilesDir(null)
            }${File.separator}${createFilePrefix(suffix)}"
        )
    }

    /**
     * /storage/emulated/0/Download/20200828_1598608685239.mp3
     *
     * @param suffix mp3/mp4
     */
    fun createFileToDownLoadDir(suffix: String): File {
        return File(
            "${Environment.getExternalStorageDirectory().absolutePath}${File.separator}Download${File.separator}${
                createFilePrefix(
                    suffix
                )
            }"
        )
    }
}