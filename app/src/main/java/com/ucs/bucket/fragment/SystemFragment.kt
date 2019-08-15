package com.ucs.bucket.fragment;

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.ucs.bucket.HistoryItemCustomview
import com.ucs.bucket.R
import com.ucs.bucket.Storage
import kotlinx.android.synthetic.main.fragment_log.view.*
import kotlinx.android.synthetic.main.fragment_system.view.*

class SystemFragment : Fragment() {
//    lateinit var area : LinearLayout
    lateinit var test_internet : CardView
    lateinit var test_printer : CardView
    lateinit var time_btn : CardView
    lateinit var back_btn : CardView


    companion object {
        fun newInstance(): SystemFragment {
            var fragment = SystemFragment()
            var args = Bundle()
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