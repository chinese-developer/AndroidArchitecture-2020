package com.example.architecture.home.ui.model.allgames

import com.example.architecture.home.ui.model.BaseModel

data class Model(override val content: String) : BaseModel()

data class DoubleItemModel(override val content: String, val coverImgUrl: String? = "") : BaseModel()

data class ThreeItemModel(override val content: String) : BaseModel()

data class FourItemModel(override val content: String) : BaseModel()

data class FiveItemModel(override val content: String) : BaseModel()

data class SixItemModel(override val content: String) : BaseModel()