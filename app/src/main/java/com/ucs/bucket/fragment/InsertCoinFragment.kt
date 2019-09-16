package com.ucs.bucket.fragment;

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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
import com.ucs.bucket.db.db.entity.BalanceLog
import com.ucs.bucket.db.db.helper.RoomConstants
import kotlinx.android.synthetic.main.fragment_insert_coin.view.*
import kotlinx.android.synthetic.main.fragment_insert_coin.view.name_user
import kotlinx.android.synthetic.main.fragment_insert_coin.view.status_offline
import kotlinx.android.synthetic.main.fragment_insert_coin.view.status_online
import kotlinx.android.synthetic.main.fragment_open.view.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class InsertCoinFragment : Fragment(),AsyncResponseCallback{




    private var db: ApplicationDatabase? = null
    private lateinit var arrayUser: List<BalanceLog>


    lateinit var textSum : TextView
    lateinit var depositText : TextView
    lateinit var textSum2 : TextView
    lateinit var one_value : TextView
    lateinit var two_value : TextView
    lateinit var five_value : TextView
    lateinit var ten_value : TextView
    lateinit var twenty_value : TextView
    lateinit var fifty_value : TextView
    lateinit var one_hunred_value : TextView
    lateinit var five_hunred_value : TextView
    lateinit var thousand_value : TextView
    lateinit var textMoney : TextView
//    lateinit var money_errors_txt : EditText
    lateinit var name_user : TextView
    lateinit var btn_cancle_coin : Button
    lateinit var btn : Button
    lateinit var testsend : Button
    lateinit var money_total_txt : TextView

    lateinit var money_errors_value : TextView
    lateinit var btn_insert_drop : Button
    lateinit var online_btn : Button
    lateinit var offline_btn : Button



    var test = 0
    var user = ""
    var balanceBefore = 0
    var dMoney = 0


    companion object {

        fun newInstance(rank: String, str: String , nameData : String): InsertCoinFragment {

            var fragment = InsertCoinFragment()
            var args = Bundle()
            args.putString("rank",rank)
            args.putString("user",str)
            args.putString("name",nameData)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var rootView = inflater!!.inflate(R.layout.fragment_insert_coin, container, false)
        initInstance(rootView)



        return rootView
    }

    fun initInstance(root: View) {

        db = context?.let { ApplicationDatabase.getInstance(it) }
        arrayUser = db?.balanceLogDao()?.getLastId()!!

//        var storage = SessionManager(context!!)
//
//        var tokenTest:HashMap<String,String> = storage.getUserDetails()
////        var username:String = user.get(SessionManager.USERNAME)!!
////        var firstname:String = user.get(SessionManager.FIRSTNAME)!!
//        var token:String = tokenTest.get(SessionManager.TOKEN)!!
//        Toast.makeText(getActivity(),token,Toast.LENGTH_SHORT).show();

        for (item in arrayUser){

            balanceBefore = item.balance!!.toInt()

        }

        testsend =root.testsend
//        depositText = root.total_deposit_value

        one_value = root.one_value
        two_value = root.two_value
        five_value = root.five_value
        ten_value = root.ten_value
        twenty_value = root.twenty_value
        fifty_value = root.fifty_value
        one_hunred_value = root.one_hunred_value
        five_hunred_value = root.five_hunred_value
        thousand_value = root.thousand_value

        money_total_txt = root.total_deposit_value
        money_errors_value = root.money_errors_value
        btn_insert_drop = root.btn_insert_drop
        offline_btn = root.status_offline
        online_btn = root.status_online

        name_user = root.name_user
//        money_errors_txt = root.money_errors_value
        textMoney = root.test_money
        textSum = root.tv_insert_sum
        textSum2 = root.tv_insert_sum2
        btn = root.btn_insert_coin
        btn_cancle_coin = root.btn_cancle_coin
        name_user.text = arguments?.getString("name")!!
        user = arguments?.getString("user")!!
        (activity as MainActivity).sendData("o$user")
//        textSum.text = "${arguments?.getString("rank")}"
        textSum.text = "${arguments?.getString("rank")}"

//        val storage = Storage(context!!)
//        var log = storage.getLog()

        if(checkNetworkConnection()){

            offline_btn.visibility = View.INVISIBLE

        }else{

            online_btn.visibility = View.INVISIBLE
        }


        btn.setOnClickListener {


            var detail_one = one_value.text.toString()
            var detail_two = two_value.text.toString()
            var detail_five = five_value.text.toString()
            var detail_ten = ten_value.text.toString()
            var detail_twenty = twenty_value.text.toString()
            var detail_fifty = fifty_value.text.toString()
            var detail_one_hunred = one_hunred_value.text.toString()
            var detail_five_hunred = five_hunred_value.text.toString()
            var detail_thousand = thousand_value.text.toString()

            val detailDeposit= JSONObject()
            detailDeposit.put("coin_1",detail_one)
            detailDeposit.put("coin_2",detail_two)
            detailDeposit.put("coin_5",detail_five)
            detailDeposit.put("coin_10",detail_ten)
            detailDeposit.put("bank_20",detail_twenty)
            detailDeposit.put("bank_50",detail_fifty)
            detailDeposit.put("bank_100",detail_one_hunred)
            detailDeposit.put("bank_500",detail_five_hunred)
            detailDeposit.put("bank_1000",detail_thousand)




            val currentDate = SimpleDateFormat("MM/dd/yyyy")
            val currentDateTime = SimpleDateFormat("MM/dd/yyyy HH:mm")
//            log+="$user,${currentDate.format(Date())},${textSum.text};"
//            storage.setLog(log)
            fragmentManager?.popBackStack()



            textSum.text.toString().trim()
//            textMoney.text.toString()

            var strDeposit:String = textSum.text.toString().replace(",","")

            var deposit:Int = strDeposit.toInt()

            var strDropMoney:String = money_errors_value.text.toString().replace(",","")

            if (strDropMoney == "-"){

                dMoney = 0

            }else{

                dMoney = strDropMoney.toInt()

            }

            var drop:Int = dMoney

            var totalDeposit = deposit + drop
            var test = "08/09/2019"




            if (checkNetworkConnection()){




                var storage = SessionSerial(context!!)

                var serial:HashMap<String,String> = storage.getUserDetails()

                var serial_value:String = serial.get(SessionSerial.SERIAL_ID)!!
                var storage2 = SessionManager(context!!)

                var token:HashMap<String,String> = storage2.getUserDetails()

                var tokenValue:String = token.get(SessionManager.TOKEN)!!

                val depositData = JSONObject()
                depositData.put("serial",serial_value)
                depositData.put("action_code","DE")
                depositData.put("detail",detailDeposit.toString())
                depositData.put("deposit",deposit)
                depositData.put("drop",drop)
                depositData.put("balance",balanceBefore + totalDeposit)

//                depositData.put("balance",balanceBefore + totalDeposit)


                val updateQueue = Volley.newRequestQueue(context)
                val url = "http://139.180.142.52/api/save/event"
                val updateReq = object : JsonObjectRequest(Request.Method.POST, url, depositData,
                    Response.Listener {response ->

                        Log.e("Success","OK")
                        val balance =
                                BalanceLog(username = user, dated = currentDate.format(Date()).trim(),datedtime = currentDateTime.format(Date()).trim(),
                                        action = "DE", deposit = deposit, drop = drop, toto_deposit = totalDeposit,
                                        balance_before = balanceBefore, balance = balanceBefore + totalDeposit, status = "N", sync = "0", open_id = 0,detail_deposit = detailDeposit.toString())

                        InsertLogAsync(db!!.balanceLogDao(), RoomConstants.INSERT_USER, this).execute(balance)

                    },
                    Response.ErrorListener {response ->

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


                val balance =
                    BalanceLog(username = user, dated = currentDate.format(Date()).trim(),datedtime = currentDateTime.format(Date()).trim(),
                        action = "DE", deposit = deposit, drop = drop, toto_deposit = totalDeposit,
                        balance_before = balanceBefore, balance = balanceBefore + totalDeposit, status = "N", sync = "1", open_id = 0,detail_deposit = detailDeposit.toString())

                InsertLogAsync(db!!.balanceLogDao(), RoomConstants.INSERT_USER, this).execute(balance)

                Toast.makeText(getActivity(),detailDeposit.toString(),Toast.LENGTH_SHORT).show();

            }


            (activity as MainActivity).sendData("c,"+"drop/${drop}"+",deposit/${deposit}"+",total/${totalDeposit}")
//            (activity as MainActivity).sendData("c"+ "test")




        }



        btn_insert_drop.setOnClickListener{

            val dialog = DropMoneyFragment()
            dialog.show(fragmentManager, "DropMoneyFragment")



        }




        testsend.setOnClickListener {
            getCoin("\nS0 0 1 2 0 0 0 0 0E")

//            getCoin("\nS0 0 1 2 0 0 0 0 10E")
        }

        btn_cancle_coin.setOnClickListener {


            fragmentManager?.popBackStack()

        }


    }

    var mString = ""
    fun getCoin(data : String){

        textSum.text = ""
//        textSum2.text = ""
        var crash = arrayOf(1,2,5,10,20,50,100,500,1000)
        var count_coin = arrayOf(0,0,0,0,0,0,0,0,0)
        mString+=data
        data.forEachIndexed { index, c ->
            if(c =='\n'){
                var temp = ""

                for ( i in 0 until mString.length){
                    if(mString[i]=='S'){
                        for(j in i+1 until mString.length ){
                            if(mString[j]!='E')
                                temp += mString[j]
                            else break
                        }
                    }
                }
                temp.trim()


                val sp = temp.split(" ")
                var index = 0
                var sum = 0



                sp.forEach {
                    val t = it.toIntOrNull()
                    if(t!=null){

                        count_coin[index] = t
                        sum += crash[index++]*t


                    }
                }


                one_value.text = count_coin[0].toString()
                two_value.text = count_coin[1].toString()
                five_value.text = count_coin[2].toString()
                ten_value.text = count_coin[3].toString()
                twenty_value.text = count_coin[4].toString()
                fifty_value.text = count_coin[5].toString()
                one_hunred_value.text = count_coin[6].toString()
                five_hunred_value.text = count_coin[7].toString()
                thousand_value.text = count_coin[8].toString()


                var total = sum
                var totalSum = String.format("%,d", total)
                textSum.text = "$totalSum"
                money_total_txt.text ="$totalSum"


                if (sum > 0){

                    btn_cancle_coin.visibility = View.INVISIBLE
                    btn.visibility = View.VISIBLE


                }

//                 sumTest(sum,count_coin)

                mString = ""

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

    override fun onResponse(isSuccess: Boolean, call: String) {

        if (call == RoomConstants.INSERT_USER) {
            if (isSuccess) {

            } else {

            }
        }
    }

//    override fun sendInput(input: String) {
//
//        money_errors_value.text = input
//        Toast.makeText(getActivity(),input, Toast.LENGTH_SHORT).show();
//
//    }

    fun checkNetworkConnection(): Boolean {

        val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun displayReceivedData(message: String) {


        var depositMoney = textSum.text.toString()
        var depositIntMoney = depositMoney.toInt()
        val dropMoneyInt = message.toInt()
        var totalBalance = dropMoneyInt + depositIntMoney

        var totalSum = String.format("%,d", totalBalance)
        var dropMoneyInt2 = String.format("%,d", dropMoneyInt)

        money_errors_value.text = "$dropMoneyInt2"
        money_total_txt.text = "$totalSum"
    }

}