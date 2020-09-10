/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.example.architecture.home.ui.model.home

import java.io.Serializable

data class Recommend(
    val id: Long,
    val name: String,
    val coverImgUrl: String? = null,
    val coverImgResource: Int
): Serializable