package com.ucs.bucket

import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.ucs.bucket.db.db.ApplicationDatabase
import com.ucs.bucket.db.db.entity.BalanceLog
import com.ucs.bucket.db.db.entity.OpenConsole
import kotlinx.android.synthetic.main.activity_user_list.*
import java.text.SimpleDateFormat
import java.util.*

class ReportOpenActivity : AppCompatActivity() {

    private lateinit var openListAdapter: OpenListAdapter
    private var db: ApplicationDatabase? = null
    private lateinit var arrayUser: List<OpenConsole>
    private var dateToday =  SimpleDateFormat("MM/dd/yyyy").format(System.currentTimeMillis())
    private lateinit var currentTime_open: TextView
    private lateinit var currentTime: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_open)
        initView()

    }


    private fun initView() {

        currentTime_open = findViewById(R.id.currentTime_deposit_op) as TextView
        currentTime_open.text = dateToday

        getListReport()

        val button = findViewById<Button>(R.id.back_btn)
        button.setOnClickListener {

            finish()
            //            supportFragmentManager.popBackStack()

        }

        setTime()
    }



    private fun setTime(){

        val previousButton: TextView  = findViewById(R.id.previousButton_op)
        val nextButton: TextView  = findViewById(R.id.nextButton_op)



        currentTime = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            currentTime.set(Calendar.YEAR, year)
            currentTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            currentTime.set(Calendar.MONTH, monthOfYear)


            val myFormat = "MM/dd/yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat)
            currentTime_open.text = sdf.format(currentTime.time)

            var chooseDate = sdf.format(currentTime.time)

            Log.d("Date = ",chooseDate)

//            textToday = sdf.format(currentTime.time)

//            if (chooseDate.equals(dateToday)){
//
////                initView(chooseDate)
//
//            }else{
//
////                var spinnerList = spinner!!.getSelectedItem().toString()
////                var statusList = ""
////                if (spinnerList.equals("ฝากเงิน")){
////
////                    statusList = "DE"
////                    getListStatus(statusList,chooseDate)
////
////                }else if (spinnerList.equals("เปิดตู้")){
////                    statusList = "OP"
////                    getListStatus(statusList,chooseDate)
////
////                }else if (spinnerList.equals("ยกเลิกการเปิดตู้")){
////                    statusList = "OF"
////                    getListStatus(statusList,chooseDate)
////
////
////                }else{
////
////                    initView(chooseDate)
////                }
//
//            }

//            initView(chooseDate)

            getListReport()


        }


        currentTime_open.setOnClickListener {
            DatePickerDialog(this@ReportOpenActivity, dateSetListener,
                currentTime.get(Calendar.YEAR),
                currentTime.get(Calendar.MONTH),
                currentTime.get(Calendar.DAY_OF_MONTH)).show()
        }

        previousButton.setOnClickListener {
            currentTime.add(Calendar.DATE, -1)


            val myFormat = "MM/dd/yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat)
            var chooseDate = sdf.format(currentTime.time)
            currentTime_open.text = sdf.format(currentTime.time)

            Log.d("Date Minus = ",chooseDate)
            getListReport()
        }

        nextButton.setOnClickListener {
            currentTime.add(Calendar.DATE, 1)
            val myFormat = "MM/dd/yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat)
            var chooseDate = sdf.format(currentTime.time)
            currentTime_open.text = sdf.format(currentTime.time)

            Log.d("Date Plus = ",chooseDate)
            getListReport()

        }





    }
    private fun getListReport(){
        var date = currentTime_open.text.toString()
//        var startTime = SimpleDateFormat("MM/dd/yyyy 00:00:00")
//        var endTime = SimpleDateFormat("MM/dd/yyyy 23:59:59")
//
//        var test1 = startTime.format(Date(date))
//        var test2 = endTime.format(Date(date))

        db = ApplicationDatabase.getInstance(this)

        recyclerUserList.layoutManager = LinearLayoutManager(this)
        openListAdapter = OpenListAdapter()

        arrayUser = db?.openDao()?.getByDate(date)!!
//        arrayUser = db?.openDao()?.getAll()!!
        openListAdapter.setUserList(arrayUser.toMutableList())
        recyclerUserList.adapter = openListAdapter


//        Log.d("StartDate  = ",test1)
//        Log.d("EndDate  = ",test2)
        openListAdapter.onItemDeleteClick = { position ->

            var oid = arrayUser[position].oid


            var i : Intent = Intent(applicationContext,DetailOpenActivity::class.java)
            i.putExtra("openid",oid.toString())
            startActivity(i)


        }


    }

}
