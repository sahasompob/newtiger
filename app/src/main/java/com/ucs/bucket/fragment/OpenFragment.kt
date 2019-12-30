package com.ucs.bucket.fragment;

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ucs.bucket.MainActivity
import com.ucs.bucket.R
import com.ucs.bucket.Util.SessionManager
import com.ucs.bucket.Util.SessionSerial
import com.ucs.bucket.appinterface.AsyncResponseCallback
import com.ucs.bucket.db.db.ApplicationDatabase
import com.ucs.bucket.db.db.dao.BalanceLogDao
import com.ucs.bucket.db.db.dao.OpenDAO
import com.ucs.bucket.db.db.entity.BalanceLog
import com.ucs.bucket.db.db.entity.OpenConsole
import com.ucs.bucket.db.db.entity.Token
import com.ucs.bucket.db.db.helper.RoomConstants
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.fragment_open.view.*
import kotlinx.android.synthetic.main.fragment_open.view.name_user
import kotlinx.android.synthetic.main.fragment_open.view.status_offline
import kotlinx.android.synthetic.main.fragment_open.view.status_online
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class OpenFragment : Fragment(), AsyncResponseCallback {

    lateinit var name_user : TextView
    lateinit var text : TextView
    lateinit var cancelBtn : Button
    lateinit var default_button : Button
    lateinit var openBtn : Button
    lateinit var op_btn : Button
    lateinit var close_btn : Button
    lateinit var online_btn : Button
    lateinit var offline_btn : Button
    private var db: ApplicationDatabase? = null
    private lateinit var arrayBalanceBefore: List<BalanceLog>
    private lateinit var arrayBalanceStatus: List<BalanceLog>
    private lateinit var tokenUser: List<Token>
    private lateinit var arrayOpenConsole: List<OpenConsole>
    private lateinit var arrayOpenData: List<OpenConsole>
    lateinit var nameBrach : TextView
    lateinit var numberConsole : TextView

    lateinit var countdownTimer2: CountDownTimer



    lateinit var camera : Camera
    lateinit var frameLayout: FrameLayout


    var user = ""
    var balanceBefore = 0
    var depositBefore = 0
    var dropBefore = 0
    var totaoDepositBefore = 0

    var balanceTest = 0
    var depositTest = 0
    var dropTest = 0
    var totaoTest = 0
    var statusTest = ""

    var openId = 0

    var log_id = 0



    var userName =""
    var idUser =0
    var openTimeConvert =""
    var balanceTotalOP =0
    var qtyDepositOP =""

    var user2 = ""
    var type=""
    var name=""
    var id=""

    companion object {
        fun newInstance(id:String,type: String, str: String, nameData : String): OpenFragment {
            var fragment = OpenFragment()
            var args = Bundle()
            args.putString("id",id)
            args.putString("type",type)
            args.putString("user",str)
            args.putString("name",nameData)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var rootView = inflater!!.inflate(R.layout.fragment_open, container, false)
        initInstance(rootView)
        return rootView
    }
    fun initInstance(root: View) {

        db = context?.let { ApplicationDatabase.getInstance(it) }
        arrayBalanceBefore = db?.balanceLogDao()?.getLastId()!!



        for (item in arrayBalanceBefore){

            depositBefore = item.deposit!!.toInt()
            dropBefore = item.drop!!.toInt()
            totaoDepositBefore = item.toto_deposit!!.toInt()
            balanceBefore = item.balance!!.toInt()

        }


        Log.d("depositBefore = ",depositBefore.toString())
        Log.d("dropBefore = ",dropBefore.toString())
        Log.d("totaoDepositBefore = ",totaoDepositBefore.toString())
        Log.d("balanceBefore = ",balanceBefore.toString())




        offline_btn = root.status_offline
        online_btn = root.status_online
        close_btn = root.close_btn
        op_btn = root.op_btn
        name_user = root.name_user
        text = root.tv_open
        cancelBtn =root.cancel_button
        openBtn =root.open_button
        default_button = root.default_button
        nameBrach = root.name_branch_op
        numberConsole = root.number_console_value_op
        name_user.text = arguments?.getString("name")!!
        user = arguments?.getString("user")!!

        user2 = arguments?.getString("user")!!
        name = arguments?.getString("name")!!
        type = arguments?.getString("type")!!
        id = arguments?.getString("id")!!


        var storage = SessionSerial(context!!)
        var serial: HashMap<String, String> = storage.getUserDetails()
        nameBrach.text =serial.get(SessionSerial.BRANCHNAME)!!
        numberConsole.text = serial.get(SessionSerial.NUMBERCONSOLE)


        getData("ready") // CountDownTimer Working

        if(checkNetworkConnection()){

            var id = (activity as MainActivity).userID()
            Log.e("IDEIEI = ",id.toString())
            offline_btn.visibility = View.INVISIBLE

        }else{

            online_btn.visibility = View.INVISIBLE
        }



        cancelBtn.setOnClickListener {




            if (checkNetworkConnection()){

//                (activity as MainActivity).setupSurfaceHolder()
                (activity as MainActivity).syncDataLogToServer()
                var id = (activity as MainActivity).userID()
                Log.e("IDEIEI = ",id.toString())

                tokenUser = db?.tokenDao()?.getToken(id)!!

                var testtoken=""

                for (item in tokenUser){

                    testtoken = item.token!!

                }

                Log.d("tokenUser = ",testtoken)



                var storage = SessionSerial(context!!) // Share references Serial Data
                var serial:HashMap<String,String> = storage.getUserDetails()
                var serial_value:String = serial.get(SessionSerial.SERIAL_ID)!!


                val depositData = JSONObject()
                depositData.put("serial",serial_value)
                depositData.put("action_code","OF")
                depositData.put("detail","")
                depositData.put("deposit",0)
                depositData.put("drop",0)
                depositData.put("balance",balanceBefore)

//                depositData.put("balance",balanceBefore + totalDeposit)


                val updateQueue = Volley.newRequestQueue(context)
                val url = "http://139.180.142.52/api/save/event"
                val updateReq = object : JsonObjectRequest(Request.Method.POST, url, depositData,
                    Response.Listener { response ->

                        Log.e("Success","OK")
                        var logID = response.getInt("log_id")
                        Log.e("LOGID IS ==",logID.toString())
                        log_id = logID

                        val sssss = SimpleDateFormat("MM/dd/yyyy")
                        val sssss2 = SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
                        val balance =
                            BalanceLog(username = user, dated = sssss.format(Date()).trim(),datedtime = sssss2.format(Date()).trim(),
                                action = "OF", deposit = depositBefore, drop = dropBefore, toto_deposit = totaoDepositBefore,
                                balance_before = balanceBefore, balance = balanceBefore, status = "-",log_id = log_id)

                        InsertLogAsync(db!!.balanceLogDao(), RoomConstants.INSERT_OPF, this).execute(balance)
//                        fragmentManager?.popBackStack()


                    },
                    Response.ErrorListener { response ->

                        Log.e("Error",response.toString())
                    }) {

                    // override getHeader for pass session to service
                    override fun getHeaders(): MutableMap<String, String> {

                        val header = mutableMapOf<String, String>()
                        // "Cookie" and "PHPSESSID=" + <session value> are default format
                        header.put("Accept", "application/json")
                        header.put("Authorization", "Bearer "+ testtoken)
                        return header
                    }
                }
                updateQueue.add(updateReq)


                getData("finish") // Cancel CountDownTimer


            }else{

//                (activity as MainActivity).setupSurfaceHolder()

                log_id = 0



                val sssss = SimpleDateFormat("MM/dd/yyyy")
                val sssss2 = SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
                val balance =
                    BalanceLog(username = user, dated = sssss.format(Date()).trim(),datedtime = sssss2.format(Date()).trim(),
                        action = "OF", deposit = depositBefore, drop = dropBefore, toto_deposit = totaoDepositBefore,
                        balance_before = balanceBefore, balance = balanceBefore, status = "-",log_id = log_id)

                InsertLogAsync(db!!.balanceLogDao(), RoomConstants.INSERT_OPF, this).execute(balance)
//                fragmentManager?.popBackStack()


            }


//            (activity as MainActivity).closeCamera()



        }


        openBtn.setOnClickListener {

            if (checkNetworkConnection()){

                (activity as MainActivity).syncDataLogToServer()


                var id = (activity as MainActivity).userID()
                Log.e("IDEIEI = ",id.toString())

                tokenUser = db?.tokenDao()?.getToken(id)!!

                var testtoken=""

                for (item in tokenUser){

                    testtoken = item.token!!

                }

                Log.d("tokenUser = ",testtoken)


                var storage = SessionSerial(context!!) // Share references Serial Data

                var serial:HashMap<String,String> = storage.getUserDetails()

                var serial_value:String = serial.get(SessionSerial.SERIAL_ID)!!
                var storage2 = SessionManager(context!!)

                val depositData = JSONObject()
                depositData.put("serial",serial_value)
                depositData.put("action_code","OP")
                depositData.put("detail","")
                depositData.put("deposit",0)
                depositData.put("drop",0)
                depositData.put("balance",0)

//                depositData.put("balance",balanceBefore + totalDeposit)


                val updateQueue = Volley.newRequestQueue(context)
                val url = "http://139.180.142.52/api/save/event"
                val updateReq = object : JsonObjectRequest(Request.Method.POST, url, depositData,
                    Response.Listener { response ->

                        Log.e("Success","OK")
                        var logID = response.getInt("log_id")
                        Log.e("LOGID IS ==",logID.toString())
                        log_id =logID



//            fragmentManager?.popBackStack()

                        val sssss = SimpleDateFormat("MM/dd/yyyy")
                        val sssss2 = SimpleDateFormat("MM/dd/yyyy HH:mm:ss")








//            arrayBalanceStatus = db?.balanceLogDao()?.getByStatus()!!
                        arrayBalanceStatus = db?.balanceLogDao()?.getDepositOpen()!!

                        var count = arrayBalanceStatus.size

                        val openAction =

                            OpenConsole(open_time = sssss2.format(Date()).trim(),open_date = sssss.format(Date()).trim(),deposit_count = count,balance_money = balanceBefore,user_open = user )

                        InsertOpenAcion(db!!.openDao(), RoomConstants.INSERT_OPEN, this).execute(openAction)






//            Toast.makeText(context, openId.toString(), Toast.LENGTH_SHORT).show()



                    },
                    Response.ErrorListener { response ->

                        Log.e("Error",response.toString())
                    }) {

                    // override getHeader for pass session to service
                    override fun getHeaders(): MutableMap<String, String> {

                        val header = mutableMapOf<String, String>()
                        // "Cookie" and "PHPSESSID=" + <session value> are default format
                        header.put("Accept", "application/json")
                        header.put("Authorization", "Bearer "+ testtoken)
                        return header
                    }
                }
                updateQueue.add(updateReq)




            }else{


               getData("finish") // Cancel CountDownTimer


                val sssss = SimpleDateFormat("ddMMyyyyHHmmss")
                val sssss2 = SimpleDateFormat("MM/dd/yyyy HH:mm:ss")








                arrayBalanceStatus = db?.balanceLogDao()?.getDepositOpen()!!

                var count = arrayBalanceStatus.size

                Log.d("Deposit_Count",count.toString())

                val openAction =

                    OpenConsole(open_time = sssss2.format(Date()).trim(),deposit_count = count,balance_money = balanceBefore,user_open = user )

                InsertOpenAcion(db!!.openDao(), RoomConstants.INSERT_OPEN, this).execute(openAction)




            }




        }


        op_btn.setOnClickListener {

        }

        close_btn.setOnClickListener {

        }

    }


    // CountDownTimer Function
    fun getData(data : String){

//        Toast.makeText(context, data, Toast.LENGTH_SHORT).show()
        if (data.trim().equals("ready")){

            var time = SessionManager(context!!).getDelay()
//        var countDownLength = time.toLong()
            var countDownLength = time*1000

            countdownTimer2 = object: CountDownTimer(countDownLength.toLong(), 1000) {

                override fun onTick(millisUntilFinished: Long) {

                    text.text = "เหลือเวลาอีก : " + millisUntilFinished / 1000 + " วินาที"
                }

                override fun onFinish() {

                            default_button.visibility = View.GONE
                            openBtn.visibility = View.VISIBLE

                            text.text = "กดปุ่มเปิดตู้"




                }
            }

            countdownTimer2.start()


        }else if (data.equals("finish")){


            countdownTimer2.onFinish() // Finish CountDownTimer
            fragmentManager?.beginTransaction()
                ?.replace(R.id.area_main,MainFragment.newInstance(id,type,user,name),"open")
                ?.addToBackStack("open")
                ?.commit()

        }


    }



    class InsertOpenAcion(private val openDao: OpenDAO, private val call: String, private val responseAsyncResponse: AsyncResponseCallback) : AsyncTask<OpenConsole, Void, OpenConsole>() {
        override fun doInBackground(vararg openConsole: OpenConsole?): OpenConsole? {
            return try {
                openDao.insertOpenConsole(openConsole[0]!!)
                openConsole[0]!!
            } catch (ex: Exception) {
                null
            }
        }

        override fun onPostExecute(result: OpenConsole?) {
            super.onPostExecute(result)
            if (result != null) {
                responseAsyncResponse.onResponse(true, call)

            } else {
                responseAsyncResponse.onResponse(false, call)
            }
        }
    }


    class InsertLogAsync(private val balanceLogDao: BalanceLogDao, private val call: String, private val responseAsyncResponse: AsyncResponseCallback) : AsyncTask<BalanceLog, Void, BalanceLog>() {
        override fun doInBackground(vararg balancelog: BalanceLog?): BalanceLog? {
            return try {
                balanceLogDao.insertAll(balancelog[0]!!)
                balancelog[0]!!
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
    }


    class UpdateAsync(private val balanceLogDao: BalanceLogDao, private val call: String, private val responseAsyncResponse: AsyncResponseCallback) : AsyncTask<BalanceLog, Void, BalanceLog>() {
        override fun doInBackground(vararg balancelog: BalanceLog?): BalanceLog? {
            return try {
                balanceLogDao.updateAll(balancelog[0]!!)
                balancelog[0]!!
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
    }


    override fun onResponse(isSuccess: Boolean, call: String) {

        if (call == RoomConstants.INSERT_USER) {

            if (isSuccess) {
//                Toast.makeText(context, "Successfully added", Toast.LENGTH_SHORT).show()
//                test()



            } else {
//                Toast.makeText(context, "Some error occur please try again later!!!", Toast.LENGTH_SHORT).show()
            }

        }else if (call == RoomConstants.INSERT_OPEN){

            if (isSuccess) {

                test()

            } else {


            }

        } else if (call == RoomConstants.INSERT_OPF){

            if (isSuccess) {
                getData("finish")
//                Toast.makeText(context, "INSERT_OPF", Toast.LENGTH_SHORT).show()

            } else {


            }

        }


    }

    // Save Open Console Method
    fun test(){



        val sssss = SimpleDateFormat("MM/dd/yyyy")
        val sssss2 = SimpleDateFormat("MM/dd/yyyy HH:mm:ss")

        arrayOpenConsole = db?.openDao()?.getLastedId()!!


        for (item in arrayOpenConsole){

            openId = item.oid!!.toInt()

        }

        for (item in arrayBalanceStatus){

            var idTest = 0
            idTest = item.bid!!.toInt()
            balanceTest = item.balance!!.toInt()
            depositTest = item.deposit!!.toInt()
            dropTest = item.drop!!.toInt()
            totaoTest = item.toto_deposit!!.toInt()
            statusTest = item.status!!


            db = ApplicationDatabase.getInstance(context!!)

            db?.balanceLogDao()?.updateOpenId(openId,idTest)!!
//                BalanceLog(bid = idTest,username = user, dated = dateTest,
//                    action = actionTest, deposit = depositTest, drop = dropTest, toto_deposit = totaoTest,
//                    balance_before = balanceBeforeTest, balance = balanceTest, status = "U", sync = "N", open_id = openId ,log_id = log_id)
//
//            UpdateAsync(db!!.balanceLogDao(), RoomConstants.UPDATE_USER, this).execute(balance)

        }

        val balance =
            BalanceLog(username = user, dated = sssss.format(Date()).trim(),datedtime = sssss2.format(Date()).trim(),
                action = "OP", deposit = 0, drop = 0, toto_deposit = balanceBefore,
                balance_before = balanceBefore, balance = 0, status = "-", sync = "N", open_id = openId,log_id = log_id)

        InsertLogAsync(db!!.balanceLogDao(), RoomConstants.INSERT_USER, this).execute(balance)





        closeData("close",openId)


    }


    /// Save CL to Server And Sqlite

    fun closeData(data : String, id_open : Int){

        val sssss = SimpleDateFormat("MM/dd/yyyy")
        val sssss2 = SimpleDateFormat("MM/dd/yyyy HH:mm:ss")



        if (data.trim().equals("close")){

            // Check Inter Before Save Data
            if (checkNetworkConnection()){

                var id = (activity as MainActivity).userID()
                Log.e("IDEIEI = ",id.toString())

                tokenUser = db?.tokenDao()?.getToken(id)!!

                var testtoken=""

                for (item in tokenUser){

                    testtoken = item.token!!

                }

                Log.d("tokenUser = ",testtoken)



                var storage = SessionSerial(context!!) // Share references Serial Data

                var serial:HashMap<String,String> = storage.getUserDetails()

                var serial_value:String = serial.get(SessionSerial.SERIAL_ID)!!



                val depositData = JSONObject()
                depositData.put("serial",serial_value)
                depositData.put("action_code","CL")
                depositData.put("detail","")
                depositData.put("deposit",0)
                depositData.put("drop",0)
                depositData.put("balance",0)

//                depositData.put("balance",balanceBefore + totalDeposit)


                val updateQueue = Volley.newRequestQueue(context)
                val url = "http://139.180.142.52/api/save/event"
                val updateReq = object : JsonObjectRequest(Request.Method.POST, url, depositData,
                    Response.Listener { response ->

                        Log.e("Success","OK")
                        var logID = response.getInt("log_id")
                        Log.e("LOGID IS ==",logID.toString())
                        log_id =logID

//                        (activity as MainActivity).sendData("unlock".trim())
//            fragmentManager?.popBackStack()

                        val sssss = SimpleDateFormat("MM/dd/yyyy")
                        val sssss2 = SimpleDateFormat("MM/dd/yyyy HH:mm:ss")




                        val balance =
                            BalanceLog(username = user, dated = sssss.format(Date()).trim(),datedtime = sssss2.format(Date()).trim(),
                                action = "CL", deposit = 0, drop = 0, toto_deposit = 0,
                                balance_before = 0, balance = 0, status = "-", sync = "N", open_id = openId,log_id = log_id)

                        InsertLogAsync(db!!.balanceLogDao(), RoomConstants.INSERT_USER, this).execute(balance)



//            Toast.makeText(context, openId.toString(), Toast.LENGTH_SHORT).show()



                    },
                    Response.ErrorListener { response ->

                        Log.e("Error",response.toString())
                    }) {

                    // override getHeader for pass session to service
                    override fun getHeaders(): MutableMap<String, String> {

                        val header = mutableMapOf<String, String>()
                        // "Cookie" and "PHPSESSID=" + <session value> are default format
                        header.put("Accept", "application/json")
                        header.put("Authorization", "Bearer "+ testtoken)
                        return header
                    }
                }
                updateQueue.add(updateReq)




            }else{


                val balance =
                    BalanceLog(username = user, dated = sssss.format(Date()).trim(),datedtime = sssss2.format(Date()).trim(),
                        action = "CL", deposit = 0, drop = 0, toto_deposit = 0,
                        balance_before = 0, balance = 0, status = "-", sync = "N", open_id = openId,log_id = 0)

                InsertLogAsync(db!!.balanceLogDao(), RoomConstants.INSERT_USER, this).execute(balance)




//                fragmentManager?.popBackStack()


            }





        }else{




        }


        db = ApplicationDatabase.getInstance(context!!)

        arrayOpenData = db?.openDao()?.getOpenDataById(id_open)!!

        var user_id : String = ""

        var openTime = SimpleDateFormat("ddMMyyyyHHmmss")
        var openTimeConvert=""

        for (item in arrayOpenData){

             idUser = item.oid!!.toInt()
             var time = item.open_time!!
             userName = item.user_open!!
             balanceTotalOP = item.balance_money!!
             qtyDepositOP = item.deposit_count!!.toString()
             openTimeConvert = openTime.format(Date(time)).trim()

        }


        Log.d("Open_ID = ",idUser.toString())
        Log.d("OpenTime = ",openTimeConvert)
        Log.d("Open_user = ",userName)
        Log.d("Open_balanceTotal = ",balanceTotalOP.toString())
        Log.d("Open_qtyDeposit = ",qtyDepositOP)



        var storage = SessionSerial(context!!)
        var serial: HashMap<String, String> = storage.getUserDetails()
        var branchName = serial.get(SessionSerial.NUMBERCONSOLE)




        (activity as MainActivity).sendData("u"+openTimeConvert+userName+"#"+balanceTotalOP+"#"+qtyDepositOP+"#"+branchName) // Send Data Open Console To Arduino


//        u#วันที่user#ผลรวมในรอบการฝาก#จำนวนรอบการฝาก

        getData("finish")


    }



    // Check Internet

    fun checkNetworkConnection(): Boolean {

        val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }



}