package com.app.base.data

import com.android.base.utils.BaseUtils
import com.android.base.utils.android.cache.SpCache
import com.android.base.utils.ktx.ifNonNull
import com.android.base.utils.ktx.ifNull
import com.android.base.utils.ktx.otherwise

object EnvironmentContext {

    private val spCache = SpCache(BaseUtils.getAppContext().packageName, false)

    private val envMap = HashMap<String, MutableList<Environment>>()

    fun startEdit(adder: EnvironmentAdder.() -> Unit) {
        adder(object : EnvironmentAdder {
            override fun add(category: String, env: Environment) {
                addEnv(category, env)
            }
        })
        endAdding()
    }

    internal fun addEnv(category: String, env: Environment) {
        envMap[category].ifNonNull {
            add(env)
        } otherwise {
            envMap[category] = mutableListOf<Environment>().apply { add(env) }
        }
    }

    private fun endAdding() {
        envMap.forEach { (category, list) ->
            val url = spCache.getString(category, "")
            list.find {
                url == it.url
            }.ifNull {
                spCache.putString(category, list[0].url)
            }
        }
    }

    fun allCategory(): Map<String, List<Environment>> {
        return envMap
    }

    fun cacheCurrentBaseUrl(category: String, url: String) {
        spCache.putString(category, url)
    }

    fun selected(category: String): Environment? {
        val url = spCache.getString(category, "")
        return envMap[category]?.find {
            url == it.url
        }
    }

    fun getPreBaseUrl(category: String): String? {
        return spCache.getString(category, "")
    }

}

interface EnvironmentAdder {
    fun add(category: String, env: Environment)
}

class Environment(val name: String, val url: String)