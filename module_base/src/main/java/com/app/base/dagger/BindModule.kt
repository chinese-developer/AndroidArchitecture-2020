package com.app.base.dagger

import com.android.base.imageloader.GlideImageLoader
import com.android.base.imageloader.ImageLoader
import com.android.base.rx.SchedulerProviderImpl
import com.android.base.rx.SchedulerProvider
import com.app.base.app.AppErrorHandler
import com.app.base.app.ErrorHandler
import com.app.base.data.app.AppDataSource
import com.app.base.data.app.AppRepository
import com.app.base.router.AppRouter
import com.app.base.router.AppRouterImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class BindModule {
    @Binds abstract fun bindErrorHandler(appErrorHandler: AppErrorHandler): ErrorHandler
    @Binds abstract fun bindAppRouter(appErrorHandler: AppRouterImpl): AppRouter
    @Binds abstract fun bindSchedulerProvider(schedulerProvider: SchedulerProviderImpl): SchedulerProvider
    @Binds abstract fun bindImageLoader(imageLoader: GlideImageLoader): ImageLoader
    @Binds abstract fun bindAppDataSource(appRepository: AppRepository): AppDataSource
}