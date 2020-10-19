package com.example.architecture.home.repository

abstract class AbsHomeModel {

    init {
        HomeModel.run {
           register()
        }
    }
}