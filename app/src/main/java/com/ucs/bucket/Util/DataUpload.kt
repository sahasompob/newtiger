package com.ucs.bucket.Util

import android.util.Log.d
import androidx.work.Worker

class DataUpload : Worker() {

    override fun doWork(): Result {
        d("daniel", "performing work now!")
        return Result.SUCCESS
    }
}