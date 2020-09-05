/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.example.architecture.home.ui.model

import java.io.Serializable

data class AlbumModel(
    val id: Long,
    val name: String,
    val coverImgUrl: String? = null,
    val coverImgResource: Int? = 0
): Serializable