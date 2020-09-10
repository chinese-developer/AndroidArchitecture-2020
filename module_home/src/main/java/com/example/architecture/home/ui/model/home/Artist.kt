package com.example.architecture.home.ui.model.home

import java.io.Serializable

data class Artist(
    val id: Long = -1,
    val name: String? = "",
    val albumCount: Int = -1,
    val songCount: Int = -1
): Serializable