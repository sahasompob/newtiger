package com.ucs.bucket.fragment;

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ucs.bucket.MainActivity
import com.ucs.bucket.R
import com.ucs.bucket.db.db.ApplicationDatabase
import com.ucs.bucket.db.db.entity.BalanceLog
import com.ucs.bucket.db.db.entity.Token
import com.ucs.bucket.db.db.helper.RoomConstants
import kotlinx.android.synthetic.main.fragment_reset_pass.view.*
import org.json.JSONObject
import org.mindrot.jbcrypt.BCrypt
import java.util.*

class ResetPasswordFragment : Fragment() {
//    lateinit var area : LinearLayout
    private var db: ApplicationDatabase? = null
    private lateinit var tokenUser: List<Token>
    lateinit var btn_save_pass : Button
    lateinit var edt_new_pass : EditText
    lateinit var btn_cancel : Button
var id = ""
    companion object {

        fun newInstance(id: String): ResetPasswordFragment {
            var fragment = ResetPasswordFragment()
            var args = Bundle()
            args.putString("id",id)
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var rootView = inflater!!.inflate(R.layout.fragment_reset_pass, container, false)
        initInstance(rootView)
        return rootView
    }

    fun initInstance(root: View) {
        db = context?.let { ApplicationDatabase.getInstance(it) }
        id = arguments?.getString("id")!!


        btn_save_pass = root.btn_save_pass_reset
        edt_new_pass = root.edt_new_pass
        btn_cancel = root.btn_cancle_reset


        btn_cancel.setOnClickListener {

            fragmentManager?.popBackStack()

        }


        btn_save_pass.setOnClickListener {


            if (checkNetworkConnection()){

                var id = (activity as MainActivity).userID()
                Log.e("IDEIEI = ",id.toString())

                tokenUser = db?.tokenDao()?.getToken(id)!!

                var testtoken=""

                for (item in tokenUser){

                    testtoken = item.token!!

                }


                var userId = id.toInt()
                var newPass = edt_new_pass.text.toString()
                var newpassGen = BCrypt.hashpw(newPass, BCrypt.gensalt())

                val depositData = JSONObject()
                depositData.put("password",newpassGen)


                val updateQueue = Volley.newRequestQueue(context)
                val url = "http://139.180.142.52/api/update/password"
                val updateReq = object : JsonObjectRequest(
                    Request.Method.POST, url, depositData,
                    Response.Listener { response ->

                        Log.e("Success",response.toString())
//                        var logID = response.getInt("log_id")
//                        Log.e("LOGID IS ==",logID.toString())
//
//                        log_id = logID

                        db?.userDao()?.updatePass(userId,newpassGen,1)!!
//                        fragmentManager?.popBackStack()

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
                        header.put("Authorization", "Bearer "+ testtoken)
                        return header
                    }
                }
                updateQueue.add(updateReq)


            }else{


                var userId = id.toInt()
                var newPass = edt_new_pass.text.toString()
                var newpassGen = BCrypt.hashpw(newPass, BCrypt.gensalt())
                db?.userDao()?.updatePass(userId,newpassGen,0)!!
                fragmentManager?.popBackStack()

            }



        }

//        area = root.area_log
//        val storage = Storage(context!!)
//        val data = storage.getLog().split(";")
//        data.forEach {
//            if(it.isNotEmpty()&&it.isNotBlank()){
//                var st = it.split(",")
//                val v = HistoryItemCustomview(context!!)
//                v.setTextNaja(st[0],st[1],st[2])
//                area.addView(v)
//            }
//        }
    }

    fun checkNetworkConnection(): Boolean {

        val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }


}