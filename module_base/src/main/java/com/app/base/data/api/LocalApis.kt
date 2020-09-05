/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.app.base.data.api

import javax.inject.Qualifier

/** OkHttp Client */
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class AuthInterceptorOkHttpClient

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class WithoutTokenInterceptorOkHttpClient
