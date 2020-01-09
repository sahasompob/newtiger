package com.ucs.bucket.fragment;

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import kotlinx.android.synthetic.main.fragment_before_open.view.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.fragment_open.view.*
import kotlinx.android.synthetic.main.fragment_open.view.cancel_button
import kotlinx.android.synthetic.main.fragment_open.view.close_btn
import kotlinx.android.synthetic.main.fragment_open.view.default_button
import kotlinx.android.synthetic.main.fragment_open.view.name_user
import kotlinx.android.synthetic.main.fragment_open.view.op_btn
import kotlinx.android.synthetic.main.fragment_open.view.open_button
import kotlinx.android.synthetic.main.fragment_open.view.status_offline
import kotlinx.android.synthetic.main.fragment_open.view.status_online
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class BeforeOpenFragment : Fragment() {

    lateinit var name_user : TextView
    lateinit var text1 : TextView
    lateinit var op_btn : Button
    lateinit var layout : RelativeLayout
    lateinit var countdownTimer: CountDownTimer
    lateinit var nameBrach : TextView
    lateinit var numberConsole : TextView






    lateinit var camera : Camera


    var user = ""
    var type=""
    var name=""
    var id=""



    companion object {
        fun newInstance(id:String, type: String, str: String, nameData : String): BeforeOpenFragment {
            var fragment = BeforeOpenFragment()
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
        var rootView = inflater!!.inflate(R.layout.fragment_before_open, container, false)
        initInstance(rootView)
        return rootView
    }
    fun initInstance(root: View) {

        op_btn = root.op_btn_test
        name_user = root.name_user
        text1 = root.tv_open_before
        layout = root.before_layout

        nameBrach= root.name_branch_bf
        numberConsole = root.number_console_value_bf






        name_user.text = arguments?.getString("name")!!
        user = arguments?.getString("user")!!
        name = arguments?.getString("name")!!
        type = arguments?.getString("type")!!
        id = arguments?.getString("id")!!


        var storage = SessionSerial(context!!)
        var serial: HashMap<String, String> = storage.getUserDetails()
        nameBrach.text =serial.get(SessionSerial.BRANCHNAME)!!
        numberConsole.text = serial.get(SessionSerial.NUMBERCONSOLE)


        var time = SessionManager(context!!).getDelayInsertKey()
        var countDownLength = time*1000


        (activity as MainActivity).sendData("p"+time.toString())  // Send Data To Arduino


        // CountDownTimer
        countdownTimer = object: CountDownTimer(countDownLength.toLong(), 1000) {

            override fun onTick(millisUntilFinished: Long) {


                text1.text = "กรุณาบิดกุญแจภายใน \n" + millisUntilFinished / 1000 + " วินาที"
                var seconds = millisUntilFinished/1000

//                if (seconds.toInt()==5){
//
//                    (activity as MainActivity).onDataRecieve("ready")
//                }
            }

            override fun onFinish() {

//                Toast.makeText(context, "คุณไม่บิดกุญแจในระยะเวลาที่กำหนด กรุณาลองอีกครั้ง", Toast.LENGTH_SHORT).show()
                fragmentManager?.popBackStack()



            }
        }

        countdownTimer.start()

        if(checkNetworkConnection()){


//            offline_btn.visibility = View.INVISIBLE

        }else{

//            online_btn.visibility = View.INVISIBLE
        }


        op_btn.setOnClickListener {

            (activity as MainActivity).onDataRecieve("ready")
        }


    }


    // Check Data From Arduino And Go To OpenFragment
    fun insertKey(data : String){


        if (data.equals("ready")){
            countdownTimer.onFinish()
//            timer.cancel()
//            layout.visibility = View.GONE
            goToOpen()



        }else{





        }


    }



    // Go To OpenFragment
    fun goToOpen(){
        fragmentManager?.beginTransaction()?.hide(this)
        fragmentManager?.beginTransaction()
            ?.replace(R.id.area_main,OpenFragment.newInstance(id,type,user,name),"open")
            ?.addToBackStack("open")
            ?.commit()
    }





    // Check Internet

    fun checkNetworkConnection(): Boolean {

        val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }




}