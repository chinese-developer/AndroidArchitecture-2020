package com.app.base.data.app

import com.app.base.common.EventCenter
import com.app.base.data.api.NetResult
import com.app.base.data.models.LoggedInUser
import com.app.base.data.models.Song
import io.reactivex.Flowable

interface AppDataSource {

    /**获取登录用后返回的 app_token */
    fun appToken(): String?

    fun checkIfHasToken(): Boolean

    /**登录之后，保存用户数据 */
    fun saveUser(user: LoggedInUser)

    /**同步获取用户信息。大部分情况下此方法从内存中读取用户信息，如果内存中没有用户信息才会从本地缓存加载。*/
    fun user(): LoggedInUser?

    /**观察用户信息，当用户信息被修改后，总是可以得到通知，这是一个全局多播的观察者，注意在不需要观察用户信息的时候取消订阅*/
    fun observableUser(): Flowable<LoggedInUser>

    /**退出登录*/
    fun logout()

    /** 登录失效或未登录将返回 false */
    fun isLoggedIn(): Boolean

    /** 刷新 token */
    fun resetToken()

    fun eventBus(): EventCenter

    /** 通过 id 获取歌曲可播放的 url 地址 */
    suspend fun getSongUrl(id: String): NetResult<Song>

}



