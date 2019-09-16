package com.ucs.bucket.fragment;

import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import com.ucs.bucket.db.db.helper.RoomConstants
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.fragment_open.view.*
import kotlinx.android.synthetic.main.fragment_open.view.name_user
import kotlinx.android.synthetic.main.fragment_open.view.status_offline
import kotlinx.android.synthetic.main.fragment_open.view.status_online
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class OpenFragment : Fragment(), AsyncResponseCallback {

    lateinit var name_user : TextView
    lateinit var text : TextView
    lateinit var cancelBtn : Button
    lateinit var default_button : Button
    lateinit var openBtn : Button
    lateinit var op_btn : Button
    lateinit var online_btn : Button
    lateinit var offline_btn : Button
    private var db: ApplicationDatabase? = null
    private lateinit var arrayBalanceBefore: List<BalanceLog>
    private lateinit var arrayBalanceStatus: List<BalanceLog>
    private lateinit var arrayOpenConsole: List<OpenConsole>


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



    companion object {
        fun newInstance(type: String, str: String, nameData : String): OpenFragment {
            var fragment = OpenFragment()
            var args = Bundle()
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
        (activity as MainActivity).sendData("p")

        for (item in arrayBalanceBefore){

            depositBefore = item.deposit!!.toInt()
            dropBefore = item.drop!!.toInt()
            totaoDepositBefore = item.toto_deposit!!.toInt()
            balanceBefore = item.balance!!.toInt()

        }
        offline_btn = root.status_offline
        online_btn = root.status_online

        op_btn = root.op_btn
        name_user = root.name_user
        text = root.tv_open
        cancelBtn =root.cancel_button
        openBtn =root.open_button
        default_button = root.default_button

        name_user.text = arguments?.getString("name")!!
        user = arguments?.getString("user")!!


        if(checkNetworkConnection()){

            offline_btn.visibility = View.INVISIBLE

        }else{

            online_btn.visibility = View.INVISIBLE
        }

        var timer = Timer()
        var time = SessionManager(context!!).getDelay()

        text.text = "เหลือเวลาอีก ${time--} วินาที"


        cancelBtn.setOnClickListener {


            if (checkNetworkConnection()){

                var storage = SessionSerial(context!!)

                var serial:HashMap<String,String> = storage.getUserDetails()

                var serial_value:String = serial.get(SessionSerial.SERIAL_ID)!!
                var storage2 = SessionManager(context!!)

                var token:HashMap<String,String> = storage2.getUserDetails()

                var tokenValue:String = token.get(SessionManager.TOKEN)!!

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

                        log_id = 5

                        timer.cancel()

                        val sssss = SimpleDateFormat("MM/dd/yyyy")
                        val sssss2 = SimpleDateFormat("MM/dd/yyyy HH:mm")
                        val balance =
                            BalanceLog(username = user, dated = sssss.format(Date()).trim(),datedtime = sssss2.format(Date()).trim(),
                                action = "OF", deposit = depositBefore, drop = dropBefore, toto_deposit = totaoDepositBefore,
                                balance_before = balanceBefore, balance = balanceBefore, status = "-",log_id = log_id)

                        InsertLogAsync(db!!.balanceLogDao(), RoomConstants.INSERT_OPF, this).execute(balance)
                        fragmentManager?.popBackStack()


                    },
                    Response.ErrorListener { response ->

                        Log.e("Error",response.toString())
                    }) {

                    // override getHeader for pass session to service
                    override fun getHeaders(): MutableMap<String, String> {

                        val header = mutableMapOf<String, String>()
                        // "Cookie" and "PHPSESSID=" + <session value> are default format
                        header.put("Accept", "application/json")
                        header.put("Authorization", "Bearer "+ tokenValue)
                        return header
                    }
                }
                updateQueue.add(updateReq)




            }else{

                log_id = 0

                timer.cancel()

                val sssss = SimpleDateFormat("MM/dd/yyyy")
                val sssss2 = SimpleDateFormat("MM/dd/yyyy HH:mm")
                val balance =
                    BalanceLog(username = user, dated = sssss.format(Date()).trim(),datedtime = sssss2.format(Date()).trim(),
                        action = "OF", deposit = depositBefore, drop = dropBefore, toto_deposit = totaoDepositBefore,
                        balance_before = balanceBefore, balance = balanceBefore, status = "-",log_id = log_id)

                InsertLogAsync(db!!.balanceLogDao(), RoomConstants.INSERT_OPF, this).execute(balance)
                fragmentManager?.popBackStack()


            }



        }


        openBtn.setOnClickListener {

            if (checkNetworkConnection()){


                var storage = SessionSerial(context!!)

                var serial:HashMap<String,String> = storage.getUserDetails()

                var serial_value:String = serial.get(SessionSerial.SERIAL_ID)!!
                var storage2 = SessionManager(context!!)

                var token:HashMap<String,String> = storage2.getUserDetails()

                var tokenValue:String = token.get(SessionManager.TOKEN)!!

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

                        log_id =5
                        timer.cancel()
                        (activity as MainActivity).sendData("unlock".trim())
//            fragmentManager?.popBackStack()

                        val sssss = SimpleDateFormat("MM/dd/yyyy")
                        val sssss2 = SimpleDateFormat("MM/dd/yyyy HH:mm")








//            arrayBalanceStatus = db?.balanceLogDao()?.getByStatus()!!
                        arrayBalanceStatus = db?.balanceLogDao()?.getBeforeOpen()!!

                        var count = arrayBalanceStatus.size

                        val openAction =

                            OpenConsole(open_time = sssss.format(Date()).trim(),deposit_count = count,balance_money = balanceBefore,user_open = user )

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
                        header.put("Authorization", "Bearer "+ tokenValue)
                        return header
                    }
                }
                updateQueue.add(updateReq)




            }else{

                timer.cancel()
                (activity as MainActivity).sendData("unlock".trim())
//            fragmentManager?.popBackStack()

                val sssss = SimpleDateFormat("MM/dd/yyyy")
                val sssss2 = SimpleDateFormat("MM/dd/yyyy HH:mm")








//            arrayBalanceStatus = db?.balanceLogDao()?.getByStatus()!!
                arrayBalanceStatus = db?.balanceLogDao()?.getBeforeOpen()!!

                var count = arrayBalanceStatus.size

                val openAction =

                    OpenConsole(open_time = sssss.format(Date()).trim(),deposit_count = count,balance_money = balanceBefore,user_open = user )

                InsertOpenAcion(db!!.openDao(), RoomConstants.INSERT_OPEN, this).execute(openAction)



//            Toast.makeText(context, openId.toString(), Toast.LENGTH_SHORT).show()



            }




        }


        op_btn.setOnClickListener {

            getData("ready")
        }

    }

    fun getData(data : String){

        Toast.makeText(context, data, Toast.LENGTH_SHORT).show()
        if (data.trim().equals("ready")){

            var time = SessionManager(context!!).getDelay()

            text.text = "เหลือเวลาอีก ${time--} วินาที"


            var timer = Timer()

            timer.scheduleAtFixedRate( object : TimerTask(){
                override fun run() {

                    activity!!.runOnUiThread {
                        text.text = "เหลือเวลาอีก ${time--} วินาที"

                        if(time==0){

                            default_button.visibility = View.GONE
                            openBtn.visibility = View.VISIBLE

                            text.text = "กดปุ่มเปิดตู้"
                            timer.cancel()


                        }
                    }

                }

            },1000,1000)

        }else{



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

//                Toast.makeText(context, "INSERT_OPF", Toast.LENGTH_SHORT).show()

            } else {


            }

        }


    }

    fun test(){

        val sssss = SimpleDateFormat("MM/dd/yyyy")
        val sssss2 = SimpleDateFormat("MM/dd/yyyy HH:mm")

        arrayOpenConsole = db?.openDao()?.getLastedId()!!


        for (item in arrayOpenConsole){

            openId = item.oid!!.toInt()

        }

        for (item in arrayBalanceStatus){

            var idTest = 0
            var balanceBeforeTest = item.balance_before!!.toInt()
            var actionTest = item.action!!
            val dateTest = item.dated
            idTest = item.bid!!.toInt()
            balanceTest = item.balance!!.toInt()
            depositTest = item.deposit!!.toInt()
            dropTest = item.drop!!.toInt()
            totaoTest = item.toto_deposit!!.toInt()
            statusTest = item.status!!

            val balance =
                BalanceLog(bid = idTest,username = user, dated = dateTest,
                    action = actionTest, deposit = depositTest, drop = dropTest, toto_deposit = totaoTest,
                    balance_before = balanceBeforeTest, balance = balanceTest, status = "U", sync = "N", open_id = openId ,log_id = log_id)

            UpdateAsync(db!!.balanceLogDao(), RoomConstants.UPDATE_USER, this).execute(balance)

        }


        val balance =
            BalanceLog(username = user, dated = sssss.format(Date()).trim(),datedtime = sssss2.format(Date()).trim(),
                action = "OP", deposit = 0, drop = 0, toto_deposit = balanceBefore,
                balance_before = balanceBefore, balance = 0, status = "-", sync = "N", open_id = openId,log_id = log_id)

        InsertLogAsync(db!!.balanceLogDao(), RoomConstants.INSERT_USER, this).execute(balance)

        fragmentManager?.popBackStack()
    }




    fun checkNetworkConnection(): Boolean {

        val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}