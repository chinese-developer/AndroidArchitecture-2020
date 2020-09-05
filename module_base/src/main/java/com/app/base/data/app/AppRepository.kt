package com.app.base.data.app

import android.content.Context
import com.android.sdk.net.error.ErrorException
import com.app.base.common.EventCenter
import com.app.base.config.AppSettings.settingsStorage
import com.app.base.data.api.AuthTokenLocalDataSource
import com.app.base.data.api.NetResult
import com.app.base.data.models.LoggedInUser
import com.app.base.data.models.Song
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject
import javax.inject.Singleton

/** 全局仓库类实现 */
@Singleton
class AppRepository @Inject constructor(
        @ApplicationContext context: Context,
        private val localDataSource: LoginLocalDataSource,
        private val tokenLocalDataSource: AuthTokenLocalDataSource
) : AppDataSource {

    @Inject lateinit var appApi: AppApiService
    @Inject lateinit var eventbus: EventCenter

    internal val storageManager = StorageManager(context, this)
    private val stableStorage = storageManager.stableStorage()
    private val userObservable = BehaviorProcessor.create<LoggedInUser>()

    var user: LoggedInUser? = null
        private set

    init {
        localDataSource.userStorage = storageManager.userStorage()
        user = localDataSource.user
        userObservable.onNext(user)
    }

    override fun appToken(): String? = tokenLocalDataSource.authToken

    override fun user(): LoggedInUser? = user

    override fun saveUser(user: LoggedInUser) {
        this.user = user
        localDataSource.user = user
        userObservable.onNext(user)
    }

    override fun observableUser(): Flowable<LoggedInUser> = userObservable

    override fun isLoggedIn(): Boolean = user != null && user!!.token != null
    override fun resetToken() {
        tokenLocalDataSource.resetToken()
    }

    override fun eventBus(): EventCenter = eventbus

    override suspend fun getSongUrl(id: String): NetResult<Song> {
        val response = appApi.songUrl(buildingSongUrlParams(id))

        if (response.isSuccessful) {
            val data = response.body()
            if (data != null) {
                return NetResult.Success(data)
            }
        }
        return NetResult.Error(
                ErrorException(
                        response.code(),
                        response.message()
                )
        )
    }

    @Synchronized
    override fun logout() {
        user = LoggedInUser.NOT_LOGIN
        tokenLocalDataSource.clearData()
        localDataSource.logout()
        // 退出登录时，不清除用户缓存的音乐和歌词信息
//        storageManager.clearUserAssociated()
        settingsStorage().clearAll()
        userObservable.onNext(user)
    }

    private fun buildingSongUrlParams(id: String): Map<String, String> {
        return mapOf(
                "types" to "url",
                "id" to id,
                "source" to "netease"
        )
    }

}