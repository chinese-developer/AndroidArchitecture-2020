package com.example.architecture.home.ui.model

import androidx.databinding.BaseObservable

open class BaseModel(open val content: String? = "", open var checked: Boolean = false) : BaseObservable()