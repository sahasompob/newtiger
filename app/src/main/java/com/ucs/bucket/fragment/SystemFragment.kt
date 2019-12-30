package com.ucs.bucket.fragment;

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.ucs.bucket.MainActivity
import com.ucs.bucket.R
import com.ucs.bucket.Util.SessionSerial
import kotlinx.android.synthetic.main.fragment_system.*
import kotlinx.android.synthetic.main.fragment_system.view.*
import java.util.HashMap

class SystemFragment : Fragment() {
//    lateinit var area : LinearLayout
    lateinit var test_internet : CardView
    lateinit var test_printer : CardView
    lateinit var time_btn : CardView
    lateinit var back_btn : CardView
    lateinit var name_user : TextView
    lateinit var nameBrach : TextView
    lateinit var numberConsole : TextView

    lateinit var online_btn : Button
    lateinit var offline_btn : Button



    companion object {
        fun newInstance(id: String,rank: String, str: String , nameData : String): SystemFragment {
            var fragment = SystemFragment()
            var args = Bundle()
            args.putString("id",id)
            args.putString("rank",rank)
            args.putString("user",str)
            args.putString("name",nameData)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var rootView = inflater!!.inflate(R.layout.fragment_system, container, false)
        initInstance(rootView)
        return rootView
    }

    fun initInstance(root: View) {


        test_printer = root.test_printer
        test_internet = root.test_internet
        time_btn = root.time_btn
        back_btn = root.back_btn
        name_user = root.name_user
        nameBrach = root.name_branch_system
        numberConsole = root.number_console_value_system
        offline_btn = root.status_offline_st
        online_btn = root.status_online_st

        var storage = SessionSerial(context!!)
        var serial: HashMap<String, String> = storage.getUserDetails()
        nameBrach.text =serial.get(SessionSerial.BRANCHNAME)!!
        numberConsole.text = serial.get(SessionSerial.NUMBERCONSOLE)


        if(checkNetworkConnection()){

            offline_btn.visibility = View.INVISIBLE

        }else{

            online_btn.visibility = View.INVISIBLE
        }




        name_user.text = arguments?.getString("name")!!

        test_internet.setOnClickListener {

            if (checkNetworkConnection()){

                startActivityForResult( Intent(Settings.ACTION_WIRELESS_SETTINGS),0)

            }else{

                startActivityForResult( Intent(Settings.ACTION_WIRELESS_SETTINGS),0)
            }


        }

        test_printer.setOnClickListener {

            startActivityForResult( Intent(Settings.ACTION_PRINT_SETTINGS),0)

        }

        time_btn.setOnClickListener {

            startActivityForResult( Intent(Settings.ACTION_DATE_SETTINGS),0)
        }

        back_btn.setOnClickListener {

            fragmentManager?.popBackStack()

        }

    }


    fun checkNetworkConnection(): Boolean {

        val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}