package com.example.architecture.home.ui.model

import androidx.databinding.BaseObservable
import com.android.base.widget.adapter.item.ItemPosition

open class BaseModel(open val title: String, open var checked: Boolean = false) : BaseObservable() {

}

data class Model(override val title: String, override var itemPosition: Int = 0) : ItemPosition,
    BaseModel(title)

data class DoubleItemModel(override val title: String, val coverImgUrl: String? = "") : BaseModel(title)

data class ThreeItemModel(override val title: String) : BaseModel(title)

data class FourItemModel(override val title: String) : BaseModel(title)

data class FiveItemModel(override val title: String) : BaseModel(title)

data class SixItemModel(override val title: String) : BaseModel(title)