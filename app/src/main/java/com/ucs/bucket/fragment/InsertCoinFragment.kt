package com.ucs.bucket.fragment;

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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
import kotlinx.android.synthetic.main.fragment_insert_coin.view.*
import java.text.SimpleDateFormat
import java.util.*


class InsertCoinFragment : Fragment(),AsyncResponseCallback {


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
    lateinit var money_errors_txt : EditText
    lateinit var name_user : TextView
    lateinit var btn_cancle_coin : Button
    lateinit var btn : Button
    lateinit var testsend : Button


    var test = 0
    var user = ""
    var balanceBefore = 0

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


        for (item in arrayUser){

            balanceBefore = item.balance!!.toInt()

        }

        testsend =root.testsend
        depositText = root.total_deposit_value

        one_value = root.one_value
        two_value = root.two_value
        five_value = root.five_value
        ten_value = root.ten_value
        twenty_value = root.twenty_value
        fifty_value = root.fifty_value
        one_hunred_value = root.one_hunred_value
        five_hunred_value = root.five_hunred_value
        thousand_value = root.thousand_value


        name_user = root.name_user
        money_errors_txt = root.money_errors_value
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

        val storage = Storage(context!!)
        var log = storage.getLog()

        btn.setOnClickListener {

            val sssss = SimpleDateFormat("MM/dd/yyyy HH:mm")
            log+="$user,${sssss.format(Date())},${textSum.text};"
            storage.setLog(log)
            fragmentManager?.popBackStack()

//            val user =
//                User(firstName = user, lastName = textSum.text.toString().trim())
//                InsertUserAsync(db!!.userDao(), RoomConstants.INSERT_USER, this).execute(user)

            textSum.text.toString().trim()
//            textMoney.text.toString()


            var strDeposit:String = textSum.text.toString()

            var deposit:Int = strDeposit.toInt()

            var strDropMoney:String = money_errors_txt.text.toString()
            var drop:Int = strDropMoney.toInt()

            var totalDeposit = deposit + drop

            val balance =
                BalanceLog(username = user, dated = sssss.format(Date()).trim(),
                           action = "DE", deposit = deposit, drop = drop, toto_deposit = totalDeposit,
                           balance_before = balanceBefore, balance = balanceBefore + totalDeposit, status = "N")

            InsertLogAsync(db!!.balanceLogDao(), RoomConstants.INSERT_USER, this).execute(balance)

            (activity as MainActivity).sendData("c,"+"drop/${drop}"+",deposit/${deposit}"+",total/${totalDeposit}")
//            (activity as MainActivity).sendData("c"+ "test")

//            Toast.makeText(context, balanceBefore.toString()+" // convert", Toast.LENGTH_SHORT).show()

        }



        testsend.setOnClickListener {


            getCoin("\nS0 0 1 2 0 0 0 0 1E")

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


//                one_value.text = count_coin[0].toString()
//                two_value.text = count_coin[1].toString()
//                five_value.text = count_coin[2].toString()
//                ten_value.text = count_coin[3].toString()
//                twenty_value.text = count_coin[4].toString()
//                fifty_value.text = count_coin[5].toString()
//                one_hunred_value.text = count_coin[6].toString()
//                five_hunred_value.text = count_coin[7].toString()
//                thousand_value.text = count_coin[8].toString()
//                textSum.text = "$sum"

                sumTest(sum,count_coin)

                mString = ""

            }
        }


    }

    fun sumTest(sum: Int, sum2: Array<Int>){


        test += sum
        textSum2.text = "$test"
        textSum.text = "$test"
        depositText.text = "$test"

        one_value.text = sum2[0].toString()
        two_value.text = sum2[1].toString()
        five_value.text = sum2[2].toString()
        ten_value.text = sum2[3].toString()
        twenty_value.text = sum2[4].toString()
        fifty_value.text = sum2[5].toString()
        one_hunred_value.text = sum2[6].toString()
        five_hunred_value.text = sum2[7].toString()
        thousand_value.text = sum2[8].toString()

//        var bbb = sum2[8].toString()
//        Toast.makeText(getActivity(),"$bbb",Toast.LENGTH_SHORT).show();


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

}