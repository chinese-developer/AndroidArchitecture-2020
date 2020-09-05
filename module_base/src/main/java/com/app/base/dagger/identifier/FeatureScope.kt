/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */
package com.app.base.dagger.identifier

import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.RUNTIME

/**
 * Scope for a feature module.
 */
@Scope
@Retention(RUNTIME)
annotation class FeatureScope
