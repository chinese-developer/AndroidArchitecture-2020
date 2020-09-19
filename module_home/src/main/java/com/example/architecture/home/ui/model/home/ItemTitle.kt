package com.example.architecture.home.ui.model.home

import com.example.architecture.home.ui.model.BaseModel

data class ItemTitle(val name: String, override var checked: Boolean) : BaseModel(name)