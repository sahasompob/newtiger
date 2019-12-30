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
import com.ucs.bucket.ManagmentUserActivity
import com.ucs.bucket.R
import com.ucs.bucket.BalanceListActivity
import com.ucs.bucket.MainActivity
import com.ucs.bucket.Util.SessionSerial
import kotlinx.android.synthetic.main.fragment_user.view.*
import java.util.HashMap

class UserFragment : Fragment() {
    lateinit var deposit : CardView
    lateinit var openBox : CardView
    lateinit var test_system : CardView
    lateinit var test: CardView
    lateinit var manage : CardView
    lateinit var reset_pass : CardView
    lateinit var setting : CardView
    lateinit var nameUser : TextView
    lateinit var logout_btn : CardView

    lateinit var online_btn : Button
    lateinit var offline_btn : Button
    lateinit var nameBrach : TextView


    var rank = ""
    var str = ""
    var nameData = ""
    companion object {

        fun newInstance(id: String,rank: String, str: String, nameData: String): UserFragment {

            var fragment = UserFragment()
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
        var rootView = inflater!!.inflate(R.layout.fragment_user, container, false)
        initInstance(rootView)
        return rootView
    }



    fun initInstance(root: View) {

        test_system = root.test_system
        nameUser = root.name_user
        deposit = root.deposit_btn
//        openBox = root.open_btn
        test = root.test_btn
//        manage = root.manage_btn
        reset_pass = root.reset_pass_btn
        logout_btn = root.log_out_user

        offline_btn = root.user_status_offline
        online_btn = root.user_status_online
        nameBrach = root.name_branch
//        setting = root.img_setting

        if(checkNetworkConnection()){

            offline_btn.visibility = View.INVISIBLE

        }else{

            online_btn.visibility = View.INVISIBLE
        }

        rank = arguments?.getString("rank")!!
        str = arguments?.getString("user")!!
        nameData = arguments?.getString("name")!!
        nameUser.text = arguments?.getString("name")!!
        var user_id = arguments?.getString("id")!!


        var storage = SessionSerial(context!!)
        var serial: HashMap<String, String> = storage.getUserDetails()
        nameBrach.text =serial.get(SessionSerial.BRANCHNAME)!!



        deposit.setOnClickListener {

            fragmentManager?.beginTransaction()
                ?.replace(R.id.area_main,InsertCoinFragment.newInstance(user_id,rank,str,nameData),"coin")
                ?.addToBackStack("coin")
                ?.commit()

            Toast.makeText(context, rank, Toast.LENGTH_SHORT).show()
        }


        reset_pass.setOnClickListener {

            fragmentManager?.beginTransaction()
                ?.replace(R.id.area_main,ResetPasswordFragment.newInstance(user_id),"resetPass")
                ?.addToBackStack("resetPass")
                ?.commit()
        }


        test.setOnClickListener {

            activity?.let{
                val intent = Intent (it, BalanceListActivity::class.java)
                it.startActivity(intent)
            }
        }


        test_system.setOnClickListener {
                fragmentManager?.beginTransaction()
                    ?.replace(R.id.area_main,SystemFragment.newInstance(id.toString(),rank,str,nameData),"testSystem")
                    ?.addToBackStack("testSystem")
                    ?.commit()

        }

        logout_btn.setOnClickListener {

            (activity as MainActivity).closeApp()

        }



    }

    fun checkNetworkConnection(): Boolean {

        val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}