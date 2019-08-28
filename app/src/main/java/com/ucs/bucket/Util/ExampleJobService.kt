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
import org.json.JSONObject


class ExampleJobService : JobService() {
    private var jobCancelled = false

    override fun onStartJob(params: JobParameters): Boolean {
        Log.d(TAG, "Job started")
        doBackgroundWork(params)

        return true
    }

    private fun doBackgroundWork(params: JobParameters) {
        loginToServer()
//        Thread(Runnable {
//
//            for (i in 0..9) {
//                Log.d(TAG, "run: $i")
//                if (jobCancelled) {
//                    return@Runnable
//                }
//
//                try {
//
//                    Thread.sleep(1000)
//                } catch (e: InterruptedException) {
//                    e.printStackTrace()
//                }
//
//            }
//
//            Log.d(TAG, "Job finished")
//
////        Log.d(TAG, "Tonla")
//            jobFinished(params, false)
//        }).start()

        jobFinished(params, false)
    }

    private fun loginToServer(){

        Log.d(TAG, "loginToServer")

//        val userAndPass= JSONObject()
//        userAndPass.put("username",user)
//        userAndPass.put("password",pass)
//
////        Toast.makeText(context, userAndPass.toString(), Toast.LENGTH_SHORT).show()
//
//        val queue = Volley.newRequestQueue(this)
//        val url = "http://139.180.142.52:3310/login"
//
//        val stringReq = JsonObjectRequest(
//            Request.Method.POST, url,userAndPass,
//            Response.Listener<JSONObject> { response ->
//
//
//                var userId = response.getJSONObject("login").getString("userid").toString()
//                var username = response.getJSONObject("login").getString("username")
//                var firstname = response.getJSONObject("login").getString("firstname")
//                var lastname = response.getJSONObject("login").getString("lastname")
//                var email = response.getJSONObject("login").getString("email")
//                var tokenData = response.getJSONObject("login").getString("token")
//
//
//
//
////                val dataJson= JSONObject()
////                dataJson.put("userid",userId)
////                dataJson.put("username",username)
////                dataJson.put("firstname",firstname)
////                dataJson.put("lastname",lastname)
////                dataJson.put("email",email)
////                dataJson.put("token",tokenData)
//
////                var strResp = response.toString()
////                val jsonObj: JSONObject = JSONObject(strResp)
////                Toast.makeText(this, dataJson.toString(), Toast.LENGTH_SHORT).show()
//
////                val jsonArray: JSONArray = jsonObj.getJSONArray("items")
////                var str_user: String = ""
//
////                for (i in 0 until jsonObj.length()) {
////                    var jsonInner: JSONObject = jsonObj.getJSONObject(i)
////                    str_user = str_user + "\n" + jsonInner.get("login")
////                }
//
//            },
//            Response.ErrorListener {
//                //                textView!!.text = "That didn't work!"
////                Log.d("TAG", "Connect Falied")
//                Toast.makeText(this, "Connect Falied", Toast.LENGTH_SHORT).show()
//
//
//            })
//        queue.add(stringReq)

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