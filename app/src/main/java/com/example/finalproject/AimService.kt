package com.example.finalproject

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.concurrent.TimeUnit


class AimService : Service() {

    val LOG_TAG = "myLogs"

    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, "onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(LOG_TAG, "onStartCommand")
        someTask()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOG_TAG, "onDestroy")
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(LOG_TAG, "onBind")
        return null
    }

    fun someTask() {
        for (i in 1..5) {
            Log.d(LOG_TAG, "i = $i")
            try {
                TimeUnit.SECONDS.sleep(1)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }
}
