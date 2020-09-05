/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.app.base.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import androidx.annotation.NonNull
import com.android.base.utils.ktx.tryCatchAll
import com.app.base.AppContext
import com.app.base.data.models.Song
import com.blankj.utilcode.util.GsonUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

class DownLoadJobService : JobService() {

    private lateinit var handler: JobServiceHandler
    private lateinit var handlerThread: HandlerThread

    private class JobServiceHandler constructor(
            @NonNull looper: Looper,
            @NonNull private val jobFinished: (JobParameters) -> Unit
    ) : Handler(looper) {
        private val appDataSource by lazy { AppContext.appDataSource() }
        private var onlyOnceRequestJob: Job? = null

        override fun handleMessage(msg: Message) {
            tryCatchAll {
                when (msg.what) {
                    HANDLER_MSG_WHAT_DOWNLOAD -> {
                        // only allow one login at a time
                        if (onlyOnceRequestJob?.isActive == true) return@handleMessage

                        val params = msg.obj as JobParameters
                        with(params.extras) {
                            val songJson = getString(KEY_FOR_DOWNLOAD_SONG_DATA)
                                    ?: return@handleMessage
                            val songListFocusOn = getString(KEY_FOR_WHICH_FRAGMENT)

                            val song = GsonUtils.fromJson(songJson, Song::class.java)
                            onlyOnceRequestJob = GlobalScope.launch {
                                fetchSongUrlAndPost(song, songListFocusOn)
                                jobFinished(params)
                            }
                        }
                    }
                }
            }
        }

        private suspend fun fetchSongUrlAndPost(song: Song, songListFocusOn: String?) {
            val response = appDataSource.getSongUrl(song.sourceOfSongId.toString())
            response.whenSuccess {
                val newSong = createSong(song, it.url)
                EventBus.getDefault().post(EventJobService().apply { setSong(newSong, songListFocusOn) })
            }
            response.whenFailure {
                val newSong = createSong(song, "")
                EventBus.getDefault().post(EventJobService().apply { setSong(newSong, songListFocusOn) })
            }
        }

        private fun createSong(song: Song, url: String?): Song {
            return Song(url).apply {
                sourceOfSongId = song.sourceOfSongId
                title = song.title
                singer = song.singer
                albumCoverImgId = song.albumCoverImgId
                albumCoverImgUrl = song.albumCoverImgUrl
            }
        }

        fun release() {
            onlyOnceRequestJob = null
        }
    }

    override fun onStartJob(params: JobParameters): Boolean {
        //  current thread is main thread ..
        val msg = handler.obtainMessage()
        msg.what = params.jobId
        msg.obj = params
        msg.sendToTarget()
        return true // if true, need to call jobFinished() method stop job which means tasks continues, otherwise the task finished already.
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false // stop when error
    }

    override fun onCreate() {
        super.onCreate()
        handlerThread = HandlerThread("DownLoadJobService_thread")
        handlerThread.start()
        handler = JobServiceHandler(handlerThread.looper) {
            jobFinished(it, false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.release()
        handler.removeCallbacksAndMessages(0)
    }

    companion object {
        const val KEY_FOR_DOWNLOAD_SONG_DATA = "key_for_download_song_data"
        const val KEY_FOR_WHICH_FRAGMENT = "key_for_which_fragment"
        const val HANDLER_MSG_WHAT_DOWNLOAD = 0x110
    }
}