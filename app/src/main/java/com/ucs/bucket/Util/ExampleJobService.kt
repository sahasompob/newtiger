package com.ucs.bucket.Util

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ucs.bucket.MainActivity
import com.ucs.bucket.db.db.ApplicationDatabase
import com.ucs.bucket.db.db.entity.BalanceLog
import com.ucs.bucket.db.db.helper.RoomConstants
import com.ucs.bucket.fragment.InsertCoinFragment
import org.json.JSONObject
import java.util.*


class ExampleJobService : JobService() {
    private var jobCancelled = false
    private var db: ApplicationDatabase? = null
    private lateinit var arrayLog: List<BalanceLog>

    override fun onStartJob(params: JobParameters): Boolean {
        Log.d(TAG, "Job started")
        doBackgroundWork(params)

        return true
    }

    private fun doBackgroundWork(params: JobParameters) {



        jobFinished(params, false)
    }





    override fun onStopJob(params: JobParameters): Boolean {
        Log.d(TAG, "Job cancelled before completion")
        jobCancelled = true
        return true
    }

    companion object {
        private val TAG = "ExampleJobService"
    }
}