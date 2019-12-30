package com.ucs.bucket

import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.ucs.bucket.appinterface.AsyncResponseCallback
import com.ucs.bucket.base.BaseActivity
import com.ucs.bucket.db.db.ApplicationDatabase
import com.ucs.bucket.db.db.entity.BalanceLog
import com.ucs.bucket.db.db.helper.RoomConstants
import kotlinx.android.synthetic.main.activity_user_list.*
import java.text.SimpleDateFormat
import java.util.*

class ReportDepositActivity : BaseActivity(), AsyncResponseCallback {

    override fun onResponse(isSuccess: Boolean, call: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var balanceListAdapter: BalanceListAdapter
    private lateinit var back_btn: Button
    private var db: ApplicationDatabase? = null
    private lateinit var arrayUser: List<BalanceLog>
    private var dateToday =  SimpleDateFormat("MM/dd/yyyy").format(System.currentTimeMillis())
    private lateinit var currentTime_deposit: TextView
    private lateinit var currentTime: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_deposit)

        initView()
    }

    private fun initView() {

        currentTime_deposit = findViewById(R.id.currentTime_deposit) as TextView
        currentTime_deposit.text = dateToday
//        back_btn = findViewById(R.id.back_btn_rd)
        getListReport()
        val button = findViewById<Button>(R.id.back_btn_rd)
        button.setOnClickListener {

            //            supportFragmentManager.popBackStack()
            finish()
        }

//        back_btn.setOnClickListener {
//
//
//
//
//        }
        setTime()
    }


    private fun setTime(){

        val previousButton: TextView  = findViewById(R.id.previousButton_dp)
        val nextButton: TextView  = findViewById(R.id.nextButton_dp)



            currentTime = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            currentTime.set(Calendar.YEAR, year)
            currentTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            currentTime.set(Calendar.MONTH, monthOfYear)


            val myFormat = "MM/dd/yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat)
            currentTime_deposit.text = sdf.format(currentTime.time)

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


        currentTime_deposit.setOnClickListener {
            DatePickerDialog(this@ReportDepositActivity, dateSetListener,
                currentTime.get(Calendar.YEAR),
                currentTime.get(Calendar.MONTH),
                currentTime.get(Calendar.DAY_OF_MONTH)).show()
        }

        previousButton.setOnClickListener {
            currentTime.add(Calendar.DATE, -1)


            val myFormat = "MM/dd/yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat)
            var chooseDate = sdf.format(currentTime.time)
            currentTime_deposit.text = sdf.format(currentTime.time)

            Log.d("Date Plus = ",chooseDate)
            getListReport()
        }

        nextButton.setOnClickListener {
            currentTime.add(Calendar.DATE, 1)
            val myFormat = "MM/dd/yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat)
            var chooseDate = sdf.format(currentTime.time)
            currentTime_deposit.text = sdf.format(currentTime.time)

            Log.d("Date Minus = ",chooseDate)
            getListReport()

        }





    }
    private fun getListReport(){

        db = ApplicationDatabase.getInstance(this)

        recyclerUserList.layoutManager = LinearLayoutManager(this)
        balanceListAdapter = BalanceListAdapter()
        arrayUser = db?.balanceLogDao()?.getByAction("DE",currentTime_deposit.text.toString())!!
        balanceListAdapter.setUserList(arrayUser.toMutableList())
        recyclerUserList.adapter = balanceListAdapter


    }


}
