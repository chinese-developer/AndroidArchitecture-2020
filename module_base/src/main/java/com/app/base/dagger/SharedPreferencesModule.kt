/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.app.base.dagger

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object SharedPreferencesModule {

    private const val DaMi_PREF = "DaMi_PREF"

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.applicationContext.getSharedPreferences(DaMi_PREF, Context.MODE_PRIVATE)
    }
}
