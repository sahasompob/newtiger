package com.ucs.bucket

import android.app.DatePickerDialog
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.*
import com.ucs.bucket.Util.DateTimeStrategy
import com.ucs.bucket.appinterface.AsyncResponseCallback
import com.ucs.bucket.base.BaseActivity
import com.ucs.bucket.db.db.ApplicationDatabase
import com.ucs.bucket.db.db.dao.BalanceLogDao
import com.ucs.bucket.db.db.entity.BalanceLog
import com.ucs.bucket.db.db.helper.RoomConstants
import com.ucs.bucket.fragment.LoginFragment

import kotlinx.android.synthetic.main.activity_user_list.*
import java.text.SimpleDateFormat
import java.util.*

class BalanceListActivity : BaseActivity(), AsyncResponseCallback, AdapterView.OnItemSelectedListener {

    private var datePicker: DatePickerDialog? = null
    private lateinit var currentDate : TextView

    private lateinit var balanceListAdapter: BalanceListAdapter
    private var db: ApplicationDatabase? = null
    private lateinit var arrayUser: List<BalanceLog>
    var spinner: Spinner? = null
    var textToday = ""
    var listStatus = arrayOf("ทั้งหมด","ฝากเงิน", "เปิดตู้", "ยกเลิกการเปิดตู้")

    private var dateToday =  SimpleDateFormat("MM/dd/yyyy").format(System.currentTimeMillis())



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)


        textToday = SimpleDateFormat("MM/dd/yyyy").format(System.currentTimeMillis())


        currentDate = findViewById(R.id.currentTime)

        currentDate.text = textToday
        spinner = this.spinner1
        spinner!!.setOnItemSelectedListener(this)

        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, listStatus)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinner!!.setAdapter(aa)

        setTime()

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        var valueStatus =""
        var listValue = p0!!.getItemAtPosition(p2).toString()

        if (listValue.equals("ฝากเงิน")){
            valueStatus = "DE"
            getListStatus(valueStatus,currentDate.text.toString())

        }else if (listValue.equals("เปิดตู้")){
            valueStatus = "OP"
            getListStatus(valueStatus,currentDate.text.toString())

        }else if (listValue.equals("ยกเลิกการเปิดตู้")){
            valueStatus = "OF"
            getListStatus(valueStatus,currentDate.text.toString())


        }else{

            initView(currentDate.text.toString())


        }



    }

    private fun setTime(){

        val previousButton: TextView  = findViewById(R.id.previousButton)
        val nextButton: TextView  = findViewById(R.id.nextButton)



        var currentTime = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            currentTime.set(Calendar.YEAR, year)
            currentTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            currentTime.set(Calendar.MONTH, monthOfYear)


            val myFormat = "MM/dd/yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat)
            currentDate.text = sdf.format(currentTime.time)

            var chooseDate = sdf.format(currentTime.time)


            textToday = sdf.format(currentTime.time)

            if (chooseDate.equals(dateToday)){

                initView(chooseDate)

            }else{

                var spinnerList = spinner!!.getSelectedItem().toString()
                var statusList = ""
                if (spinnerList.equals("ฝากเงิน")){

                    statusList = "DE"
                    getListStatus(statusList,chooseDate)

                }else if (spinnerList.equals("เปิดตู้")){
                    statusList = "OP"
                    getListStatus(statusList,chooseDate)

                }else if (spinnerList.equals("ยกเลิกการเปิดตู้")){
                    statusList = "OF"
                    getListStatus(statusList,chooseDate)


                }else{

                    initView(chooseDate)
                }

            }

//            initView(chooseDate)




        }


        currentDate.setOnClickListener {
            DatePickerDialog(this@BalanceListActivity, dateSetListener,
                currentTime.get(Calendar.YEAR),
                currentTime.get(Calendar.MONTH),
                currentTime.get(Calendar.DAY_OF_MONTH)).show()
        }

        previousButton.setOnClickListener {
           currentTime.add(Calendar.DATE, 1)
            update()

        }

        nextButton.setOnClickListener {

            currentTime.add(Calendar.DATE, 1)
            update()

        }





    }


    private fun initView(todayDate : String) {

        db = ApplicationDatabase.getInstance(this)

        recyclerUserList.layoutManager = LinearLayoutManager(this)
        balanceListAdapter = BalanceListAdapter()

        balanceListAdapter.onItemDeleteClick = { position ->
            DeleteUserAsync(db!!.balanceLogDao(), RoomConstants.DELETE_USER, this).execute(arrayUser[position])
        }



        arrayUser = db?.balanceLogDao()?.getAll(todayDate)!!
        balanceListAdapter.setUserList(arrayUser.toMutableList())
        recyclerUserList.adapter = balanceListAdapter

        val button = findViewById<Button>(R.id.back_btn)
        button.setOnClickListener {

//            supportFragmentManager.popBackStack()
            finish()

        }
    }


    private fun getListStatus(data : String,dateToday : String) {


//        Toast.makeText(this@BalanceListActivity, "Selected getList : "+ data, Toast.LENGTH_SHORT).show()


        db = ApplicationDatabase.getInstance(this)

        recyclerUserList.layoutManager = LinearLayoutManager(this)
        balanceListAdapter = BalanceListAdapter()

        balanceListAdapter.onItemDeleteClick = { position ->
            DeleteUserAsync(db!!.balanceLogDao(), RoomConstants.DELETE_USER, this).execute(arrayUser[position])
        }

        arrayUser = db?.balanceLogDao()?.getByAction(data,dateToday)!!
        balanceListAdapter.setUserList(arrayUser.toMutableList())
        recyclerUserList.adapter = balanceListAdapter

        val button = findViewById<Button>(R.id.back_btn)
        button.setOnClickListener {

            //            supportFragmentManager.popBackStack()

        }
    }

    override fun onResponse(isSuccess: Boolean, call: String) {
        if (call == RoomConstants.DELETE_USER) {
//            if (isSuccess) {
//                arrayUser = db?.balanceLogDao()?.getAll(dateToday)!!
//                balanceListAdapter.setUserList(arrayUser.toMutableList())
//                balanceListAdapter.notifyDataSetChanged()
//                Toast.makeText(this@BalanceListActivity, "Successfully deleted", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this@BalanceListActivity, "Some error occur please try again later!!!", Toast.LENGTH_SHORT).show()
//            }
        }
    }



        class DeleteUserAsync(private val balanceLogDao: BalanceLogDao, private val call: String, private val responseAsyncResponse: AsyncResponseCallback) : AsyncTask<BalanceLog, Void, BalanceLog>() {
            override fun doInBackground(vararg userlog: BalanceLog?): BalanceLog? {
                return try {
                    balanceLogDao.delete(userlog[0]!!)
                    userlog[0]!!
                } catch (ex: Exception) {
                    null
                }
            }

        override fun onPostExecute(result: BalanceLog?) {
            super.onPostExecute(result)
            if (result != null) {
                responseAsyncResponse.onResponse(true, call)
            } else {
                responseAsyncResponse.onResponse(false, call)
            }
        }

//
//        override fun onPostExecute(result: User?) {
//            super.onPostExecute(result)
//            if (result != null) {
//                responseAsyncResponse.onResponse(true, call)
//            } else {
//                responseAsyncResponse.onResponse(false, call)
//            }
//        }


    }

//    override fun onBackPressed() {
//
////        val fragment = supportFragmentManager.fragments
////        if(fragment.size==1){
////            super.onBackPressed()
////        }else{
////            supportFragmentManager.popBackStack()
////        }
//    }


fun update(){

    setTime()
}

}
