package com.example.architecture.home.repository

import com.app.base.toast
import javax.inject.Inject

class NetworkModel @Inject constructor() : AbsHomeModel() {

    fun println() {
        toast("networkModel")
    }
}