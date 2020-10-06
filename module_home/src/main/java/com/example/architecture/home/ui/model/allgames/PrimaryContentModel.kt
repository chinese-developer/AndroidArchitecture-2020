package com.example.architecture.home.ui.model.allgames

import com.example.architecture.home.ui.model.BaseModel

data class PrimaryContentModel(override val content: String?, override var checked: Boolean) :
    BaseModel()