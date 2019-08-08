package com.ucs.bucket.fragment;

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.ucs.bucket.MainActivity
import com.ucs.bucket.R
import com.ucs.bucket.Storage
import com.ucs.bucket.appinterface.AsyncResponseCallback
import com.ucs.bucket.db.db.ApplicationDatabase
import com.ucs.bucket.db.db.dao.BalanceLogDao
import com.ucs.bucket.db.db.entity.BalanceLog
import com.ucs.bucket.db.db.helper.RoomConstants
import kotlinx.android.synthetic.main.fragment_open.view.*
import org.json.JSONException
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
    private var db: ApplicationDatabase? = null
    private lateinit var arrayBalanceBefore: List<BalanceLog>
    private lateinit var arrayBalanceStatus: List<BalanceLog>


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
        op_btn = root.op_btn
        name_user = root.name_user
        text = root.tv_open
        cancelBtn =root.cancel_button
        openBtn =root.open_button
        default_button = root.default_button

        name_user.text = arguments?.getString("name")!!
        user = arguments?.getString("user")!!



        var timer = Timer()
        var time = Storage(context!!).getDelay()

        text.text = "เหลือเวลาอีก ${time--} วินาที"


        cancelBtn.setOnClickListener {

            timer.cancel()

            val sssss = SimpleDateFormat("MM/dd/yyyy")
            val sssss2 = SimpleDateFormat("MM/dd/yyyy HH:mm")
            val balance =
                BalanceLog(username = user, dated = sssss.format(Date()).trim(),datedtime = sssss2.format(Date()).trim(),
                    action = "OPF", deposit = depositBefore, drop = dropBefore, toto_deposit = totaoDepositBefore,
                    balance_before = balanceBefore, balance = balanceBefore, status = "-")

            InsertLogAsync(db!!.balanceLogDao(), RoomConstants.INSERT_USER, this).execute(balance)
            fragmentManager?.popBackStack()

        }


        openBtn.setOnClickListener {

            timer.cancel()
            (activity as MainActivity).sendData("unlock".trim())
//            fragmentManager?.popBackStack()

            val sssss = SimpleDateFormat("MM/dd/yyyy")
            val sssss2 = SimpleDateFormat("MM/dd/yyyy HH:mm")
            val balance =
                BalanceLog(username = user, dated = sssss.format(Date()).trim(),datedtime = sssss2.format(Date()).trim(),
                    action = "OP", deposit = 0, drop = 0, toto_deposit = balanceBefore,
                    balance_before = balanceBefore, balance = 0, status = "-")

            InsertLogAsync(db!!.balanceLogDao(), RoomConstants.INSERT_USER, this).execute(balance)





            arrayBalanceStatus = db?.balanceLogDao()?.getByStatus()!!
//
//            for (item in arrayBalanceStatus){
//
//                var idTest = 0
//                var balanceBeforeTest = item.balance_before!!.toInt()
//                var actionTest = item.action!!
//                val dateTest = item.dated
//                idTest = item.bid!!.toInt()
//                balanceTest = item.balance!!.toInt()
//                depositTest = item.deposit!!.toInt()
//                dropTest = item.drop!!.toInt()
//                totaoTest = item.toto_deposit!!.toInt()
//                statusTest = item.status!!
//
//                val balance =
//                    BalanceLog(bid = idTest,username = user, dated = dateTest,
//                        action = actionTest, deposit = depositTest, drop = dropTest, toto_deposit = totaoTest,
//                        balance_before = balanceBeforeTest, balance = balanceTest, status = "U")
//
//                UpdateAsync(db!!.balanceLogDao(), RoomConstants.UPDATE_USER, this).execute(balance)
//
//                val testData = JSONObject()
//                    try {
//                        testData.put("type", "d")
//                        testData.put("username", user)
//                        testData.put("date", item.dated)
//                        testData.put("deposit", depositTest)
//                        testData.put("drop", depositTest)
//                        testData.put("total_deposit", totaoTest)
//                        testData.put("balance_before", balanceBeforeTest)
//                        testData.put("balance", balanceTest)
//
//                        } catch (e: JSONException) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace()
//                        }
//
////                Toast.makeText(context, dateTest, Toast.LENGTH_SHORT).show()
////                    (activity as MainActivity).sendData(testData.toString())
//
//            }


        }


        op_btn.setOnClickListener {

            getData("ready")
        }

    }

    fun getData(data : String){

        Toast.makeText(context, data, Toast.LENGTH_SHORT).show()
        if (data.trim().equals("ready")){

            var time = Storage(context!!).getDelay()

            text.text = "เหลือเวลาอีก ${time--} วินาที"


            var timer = Timer()

            timer.scheduleAtFixedRate( object : TimerTask(){
                override fun run() {

                    activity!!.runOnUiThread {
                        text.text = "เหลือเวลาอีก ${time--} วินาที"

                        if(time==0){

                            default_button.visibility = View.GONE
                            openBtn.visibility = View.VISIBLE

                            timer.cancel()


                        }
                    }

                }

            },1000,1000)

        }else{



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
            } else {
//                Toast.makeText(context, "Some error occur please try again later!!!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}