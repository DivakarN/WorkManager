package com.sysaxiom.workmanager.periodicrequest

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.core.app.NotificationCompat
import android.app.NotificationManager
import android.app.NotificationChannel
import com.sysaxiom.workmanager.datasharing.DataSharingActivity

class MyPeriodicWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    companion object {
        const val KEY_TASK_OUT = "KEY_TASK_OUT"
    }

    override fun doWork(): Result {

        val inputData = inputData

        displayNotification("My Worker", inputData.getString(DataSharingActivity.KEY_TASK_IN).toString())

        return Result.retry()
    }

    private fun displayNotification(title: String, task: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channelID",
                "Channel Name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, "channelID")
            .setContentTitle(title)
            .setContentText(task)
            .setSmallIcon(com.sysaxiom.workmanager.R.drawable.ic_android)

        notificationManager.notify(1, notification.build())
    }

}