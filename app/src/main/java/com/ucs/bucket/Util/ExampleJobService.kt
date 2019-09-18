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

        sendDataToServer()
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

    private fun sendDataToServer(){
        Log.d(TAG, "sendDataToServer")
        db = ApplicationDatabase.getInstance(this)
        arrayLog = db?.balanceLogDao()?.getLogOffline()!!


//        Log.d("Data = ", arrayLog.toString())

        if (arrayLog.isEmpty()){


            Log.d("Data = ","Array is Null")

        }else{

            for (item in arrayLog){

                var user_id = item.bid!!
                var username = item.username!!.toString()
                var deposit = item.deposit!!.toString()
                var drop = item.drop!!.toString()
                var balanceTotal = item.balance!!.toString()
                var action_code = item.action!!.toString()
                var detail_deposit = item.detail_deposit!!.toString()
                var log_id = item.log_id!!.toString()

//                Log.d("User = ",username)
//                Log.d("Deposit = ",deposit)
//                Log.d("Total = ",balanceTotal)
//                Log.d("Log = ",log_id)






                var storage = SessionSerial(applicationContext!!)

                var serial: HashMap<String, String> = storage.getUserDetails()

                var serial_value:String = serial.get(SessionSerial.SERIAL_ID)!!
                var veryfy_code:String = serial.get(SessionSerial.VERIFYCODE)!!
//                var storage2 = SessionManager(applicationContext!!)
//
//                var token: HashMap<String, String> = storage2.getUserDetails()
//
//                var tokenValue:String = token.get(SessionManager.TOKEN)!!


                val depositData = JSONObject()
                depositData.put("serial",serial_value)
                depositData.put("username",username)
                depositData.put("action_code",action_code)
                depositData.put("verification_code",veryfy_code)
                depositData.put("detail",detail_deposit)
                depositData.put("deposit",deposit)
                depositData.put("drop",drop)
                depositData.put("balance",balanceTotal)

//                depositData.put("balance",balanceBefore + totalDeposit)


                val updateQueue = Volley.newRequestQueue(applicationContext)
                val url = "http://139.180.142.52/api/save/offline"
                val updateReq = object : JsonObjectRequest(Request.Method.POST, url, depositData,
                    Response.Listener {response ->

                        Log.e("Success","OK")
                        var logID = response.getInt("log_id")



                        db?.balanceLogDao()?.updateLogID(logID,user_id)!!

                        Log.e("Update Log ID  = ","Success")

//                        log_id = logID


//                        val balance =
//                            BalanceLog(username = user, dated = currentDate.format(Date()).trim(),datedtime = currentDateTime.format(
//                                Date()
//                            ).trim(),
//                                action = "DE", deposit = deposit, drop = drop, toto_deposit = totalDeposit,
//                                balance_before = balanceBefore, balance = balanceBefore + totalDeposit, status = "N", sync = "0", open_id = 0,detail_deposit = detailDeposit.toString(),log_id = log_id)
//
//                        InsertCoinFragment.InsertLogAsync(db!!.balanceLogDao(), RoomConstants.INSERT_USER, this).execute(balance)

                    },
                    Response.ErrorListener {response ->

                        Log.e("Error",response.toString())
                    }) {

                    // override getHeader for pass session to service
//                    override fun getHeaders(): MutableMap<String, String> {
//
//                        val header = mutableMapOf<String, String>()
//                        // "Cookie" and "PHPSESSID=" + <session value> are default format
//                        header.put("Accept", "application/json")
//                        header.put("Authorization", "Bearer "+ tokenValue)
//                        return header
//                    }
                }
                updateQueue.add(updateReq)
            }

        }


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