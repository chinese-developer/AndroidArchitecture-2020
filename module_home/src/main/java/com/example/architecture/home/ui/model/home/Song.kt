package com.example.architecture.home.ui.model.home

import java.io.Serializable

data class Song(
    val id: Long = -1,
    val albumId: Long = -1,
    val artistId: Long = -1,
    val title: String? = "",
    val artistName: String? = "",
    val albumName: String? = "",
    val duration: Int = -1,
    val trackNumber: Int = -1
): Serializable