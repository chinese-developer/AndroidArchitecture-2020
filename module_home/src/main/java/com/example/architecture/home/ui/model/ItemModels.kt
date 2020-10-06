package com.example.architecture.home.ui.model

import com.android.base.widget.adapter.item.ItemPosition

data class Model(override val content: String, override var itemPosition: Int = 0) : ItemPosition, BaseModel(content)

data class DoubleItemModel(override val content: String, val coverImgUrl: String? = "") : BaseModel(content)

data class ThreeItemModel(override val content: String) : BaseModel(content)

data class FourItemModel(override val content: String) : BaseModel(content)

data class FiveItemModel(override val content: String) : BaseModel(content)

data class SixItemModel(override val content: String) : BaseModel(content)