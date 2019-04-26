package com.allvens.instaBatch.assets.notification_system

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import com.allvens.instaBatch.R

class Notification_Manager(val context: Context){

    private lateinit var notificationManager:NotificationManager
    private lateinit var notification: Notification.Builder

    private val MID = 100
    private val ANDROID_CHANNEL_ID = "com.android.InstaBatch"
    private val ANDROID_CHANNEL_NAME = "Insta Screen Crop"

    fun updateProgressNotification(totalValue: Int, value: Int, progressValue: Int){
        notification.setProgress(totalValue, value, false)
        notification.setContentText("$progressValue %")
        build()
    }

    private fun build(){
        notificationManager.notify(MID, notification.build())
    }

    fun cancelNotification(){
        notificationManager.cancel(MID)
    }

    fun createFinishNotification(){
        notification.setContentText(context.resources.getString(R.string.loadCropImages_notificationTitle_Done))
            .setProgress(0,0,false)
            .setOngoing(false)
        build()
    }

    fun createProgressNotification(NOTIFICATION_TITLE: String, NOTIFICATION_MESSAGE: String, maxProgress: Int) {
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val am: ActivityManager = context.getSystemService(AppCompatActivity.ACTIVITY_SERVICE) as ActivityManager
        val tasks = am.getRunningTasks(1)
        val task = tasks.get(0)
        val rootActivity = task.baseActivity

        val notificationIntent = Intent()
        notificationIntent.component = rootActivity
        notificationIntent.action = Intent.ACTION_MAIN
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val pendingIntent = PendingIntent.getActivity(context, MID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val androidChannel = NotificationChannel(ANDROID_CHANNEL_ID,
                ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            )

            androidChannel.enableLights(true)
            androidChannel.enableVibration(true)
            androidChannel.lightColor = Color.GREEN
            androidChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

            val mManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mManager.createNotificationChannel(androidChannel)

            notification = Notification.Builder(context, ANDROID_CHANNEL_ID)
                .setContentTitle(NOTIFICATION_TITLE)
                .setContentText(NOTIFICATION_MESSAGE)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOnlyAlertOnce(true)
                .setPriority(Notification.PRIORITY_LOW)
                .setProgress(maxProgress, 0, false)
        } else {
            notification = Notification.Builder(context)
                .setContentTitle(NOTIFICATION_TITLE)
                .setContentText(NOTIFICATION_MESSAGE)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOnlyAlertOnce(true)
                .setPriority(Notification.PRIORITY_LOW)
                .setProgress(maxProgress, 0, false)
        }
        build()
    }
}