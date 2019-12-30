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
import kotlinx.android.synthetic.main.fragment_manager.view.*
import java.util.HashMap

class ManagerFragment : Fragment() {
    lateinit var deposit : CardView
    lateinit var openBox : CardView
    lateinit var test: CardView
    lateinit var manage : CardView
    lateinit var reset_pass : CardView
    lateinit var setting : CardView
    lateinit var nameUser : TextView
    lateinit var test_system : CardView
    lateinit var logout_btn : CardView

    lateinit var online_btn : Button
    lateinit var offline_btn : Button
    lateinit var nameBrach : TextView
    lateinit var numberConsole : TextView


    var rank = ""
    var str = ""
    var nameData = ""
    var id = ""
    companion object {

        fun newInstance(id: String,rank: String, str: String, nameData: String): ManagerFragment {

            var fragment = ManagerFragment()
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
        var rootView = inflater!!.inflate(R.layout.fragment_manager, container, false)
        initInstance(rootView)
        return rootView
    }



    fun initInstance(root: View) {
        test_system = root.test_system
        nameUser = root.name_user
        deposit = root.deposit_btn
        openBox = root.open_btn
        test = root.test_btn
        reset_pass = root.reset_pass_btn

        setting = root.img_setting
        logout_btn = root.log_out_manager

        offline_btn = root.manager_status_offline
        online_btn = root.manager_status_online
        nameBrach = root.name_branch_mn
        numberConsole = root.number_console_value_mn

        if(checkNetworkConnection()){

            offline_btn.visibility = View.INVISIBLE

        }else{

            online_btn.visibility = View.INVISIBLE
        }

        rank = arguments?.getString("rank")!!
        str = arguments?.getString("user")!!
        nameData = arguments?.getString("name")!!
        nameUser.text = arguments?.getString("name")!!
        id = arguments?.getString("id")!!

        var storage = SessionSerial(context!!)
        var serial: HashMap<String, String> = storage.getUserDetails()
        nameBrach.text =serial.get(SessionSerial.BRANCHNAME)!!
        numberConsole.text =serial.get(SessionSerial.NUMBERCONSOLE)!!

        deposit.setOnClickListener {

            fragmentManager?.beginTransaction()
                ?.replace(R.id.area_main,InsertCoinFragment.newInstance(id,rank,str,nameData),"coin")
                ?.addToBackStack("coin")
                ?.commit()

            Toast.makeText(context, rank, Toast.LENGTH_SHORT).show()
        }

        openBox.setOnClickListener {

//            fragmentManager?.beginTransaction()
//                ?.replace(R.id.area_main,OpenFragment.newInstance(id,rank,str,nameData),"open")
//                ?.addToBackStack("open")
//                ?.commit()

            fragmentManager?.beginTransaction()
                ?.replace(R.id.area_main,BeforeOpenFragment.newInstance(id,rank,str,nameData),"bopen")
                ?.addToBackStack("bopen")
                ?.commit()
        }

//        manage.setOnClickListener {
//
//            activity?.let{
//                val intent = Intent (it, ManagmentUserActivity::class.java)
//                it.startActivity(intent)
//            }
//
//
//        }

        reset_pass.setOnClickListener {

            fragmentManager?.beginTransaction()
                ?.replace(R.id.area_main,ResetPasswordFragment.newInstance(id),"resetPass")
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
                ?.replace(R.id.area_main,SystemFragment.newInstance(id,rank,str,nameData),"testSystem")
                ?.addToBackStack("testSystem")
                ?.commit()

        }


        logout_btn.setOnClickListener {

//            fragmentManager?.beginTransaction()
//                ?.replace(R.id.area_main,InsertCoinFragment.newInstance(id,rank,str,nameData),"coin")
//                ?.addToBackStack("coin")
//                ?.commit()
//
//            Toast.makeText(context, rank, Toast.LENGTH_SHORT).show()

            (activity as MainActivity).closeApp()
        }


    }

    fun checkNetworkConnection(): Boolean {

        val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}