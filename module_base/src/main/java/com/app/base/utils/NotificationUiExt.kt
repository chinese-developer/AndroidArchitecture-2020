package com.app.base.utils

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.widget.RemoteViews
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import com.app.base.R

fun FragmentActivity.setNotification(progress: Int, @StringRes title: Int = R.string.downloading) {
    val intent = Intent()
    val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    val remoteViews = RemoteViews(packageName, R.layout.remote_notification_view)
    remoteViews.setTextViewText(R.id.title, getString(title))
    remoteViews.setProgressBar(R.id.progress_bar, 100, progress, false)
    val notificationUtils = NotificationUtils(this)
    val manager: NotificationManager = notificationUtils.manager
    val notification: Notification = notificationUtils.setContentIntent(pendingIntent)
            .setContent(remoteViews)
            .setFlags(Notification.FLAG_AUTO_CANCEL)
            .setOnlyAlertOnce(true)
            .getNotification("来了一条消息", "下载歌曲", R.drawable.ic_app_logo)
    if (progress == 100 || progress == -1) {
        notificationUtils.clearNotification()
    } else {
        manager.notify(1, notification)
    }
}