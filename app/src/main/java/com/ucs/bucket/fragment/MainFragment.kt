package com.ucs.bucket.fragment;

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.ucs.bucket.*
import com.ucs.bucket.Util.SessionManager
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : Fragment() {
    lateinit var deposit : CardView
    lateinit var openBox : CardView
    lateinit var test_system : CardView
    lateinit var test: CardView
    lateinit var manage : CardView
    lateinit var reset_pass : CardView
    lateinit var setting : CardView
    lateinit var logOut : CardView

    lateinit var nameUser : TextView

    lateinit var online_btn : Button
    lateinit var offline_btn : Button

    var rank = ""
    var str = ""
    var nameData = ""
    companion object {

        fun newInstance(id: String,rank: String, str: String, nameData: String): MainFragment {

            var fragment = MainFragment()
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
        var rootView = inflater!!.inflate(R.layout.fragment_main, container, false)
        initInstance(rootView)
        return rootView
    }



    fun initInstance(root: View) {

        offline_btn = root.status_offline
        online_btn = root.status_online
        nameUser = root.name_user
        deposit = root.deposit_btn
        openBox = root.open_btn
        test = root.test_btn
        manage = root.manage_btn
        reset_pass = root.reset_pass_btn
        test_system = root.test_system
        logOut = root.log_out
        setting = root.img_setting

        if(checkNetworkConnection()){

            offline_btn.visibility = View.INVISIBLE

        }else{

            online_btn.visibility = View.INVISIBLE
        }

        nameUser.text = arguments?.getString("name")!!
        rank = arguments?.getString("rank")!!
        str = arguments?.getString("user")!!

        nameData = arguments?.getString("name")!!

        var user_id = arguments?.getString("id")!!

        var storage = SessionManager(context!!)

        deposit.setOnClickListener {

            fragmentManager?.beginTransaction()
                ?.replace(R.id.area_main,InsertCoinFragment.newInstance(user_id,rank,str,nameData),"coin")
                ?.addToBackStack("coin")
                ?.commit()

            Toast.makeText(context, rank, Toast.LENGTH_SHORT).show()
        }

        openBox.setOnClickListener {

            fragmentManager?.beginTransaction()
                ?.replace(R.id.area_main,OpenFragment.newInstance(rank,str,nameData),"open")
                ?.addToBackStack("open")
                ?.commit()
        }

        manage.setOnClickListener {

            activity?.let{
                val intent = Intent (it, ManagmentUserActivity::class.java)
                it.startActivity(intent)
            }


        }

        reset_pass.setOnClickListener {

            fragmentManager?.beginTransaction()
                ?.replace(R.id.area_main,ResetPasswordFragment.newInstance(user_id),"resetPass")
                ?.addToBackStack("resetPass")
                ?.commit()
        }


        test.setOnClickListener {

            activity?.let{
                val intent = Intent (it, ReportActivity::class.java)
                it.startActivity(intent)
            }
        }


        setting.setOnClickListener {

            fragmentManager?.beginTransaction()
                ?.replace(R.id.area_main,SettingFragment.newInstance(),"setting")
                ?.addToBackStack("setting")
                ?.commit()
//            else username.error = "request admin"
        }

        test_system.setOnClickListener {

            fragmentManager?.beginTransaction()
                ?.replace(R.id.area_main,SystemFragment.newInstance(),"system")
                ?.addToBackStack("system")
                ?.commit()
//            else username.error = "request admin"
        }

        logOut.setOnClickListener {

//            fragmentManager?.beginTransaction()
//                ?.replace(R.id.area_main,Camera2BasicFragment.newInstance(),"system")
//                ?.addToBackStack("system")
//                ?.commit()
//
//            var session = SessionManager(context!!)
//
//            session.logOutUser()

            (activity as MainActivity).closeApp()

        }




    }

    fun checkNetworkConnection(): Boolean {

        val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}