package com.example.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class NotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {


    override fun doWork(): Result {
        showNotification("Hello", "Đây là thông báo từ WorkManager time ${System.currentTimeMillis()}!")
        val s = System.currentTimeMillis() - MyApplication.t
        Log.e("AAA","Đây là thông báo từ WorkManager ${s}")

        runWorkManager()
        return Result.success()
    }

    private fun runWorkManager() {
        MyApplication.t = System.currentTimeMillis()
        Log.e("AAA","time ${MyApplication.t}")
        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(15, TimeUnit.SECONDS) // Trì hoãn 10s
            .build()
        WorkManager.getInstance(applicationContext).enqueue(workRequest)
    }

    private fun showNotification(title: String, message: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "work_channel",
                "WorkManager Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, "work_channel")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}