package com.example.architecture.home.ui.model.home

import java.io.Serializable

data class Album(
    val id: Long = -1,
    val title: String? = "",
    val artistName: String? = "",
    val artistId: Long = -1
): Serializable