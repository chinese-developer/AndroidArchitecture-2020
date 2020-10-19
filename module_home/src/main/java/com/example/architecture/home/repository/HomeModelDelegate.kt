package com.example.architecture.home.repository

import java.util.*
import kotlin.reflect.KProperty

object HomeModelDelegate {
    operator fun <T : AbsHomeModel> getValue(thisRef: Any, property: KProperty<*>): T {
        return HomeModel.run {
            property.name.capitalize(Locale.getDefault()).get()
        }
    }
}
