package com.ucs.bucket.fragment;

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.Fragment
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
import com.ucs.bucket.R
import com.ucs.bucket.db.db.ApplicationDatabase
import com.ucs.bucket.db.db.entity.User
import kotlinx.android.synthetic.main.fragment_login.view.*
import org.json.JSONObject

class LoginFragment : Fragment() {
    private var db: ApplicationDatabase? = null

    lateinit var username : EditText
    lateinit var password : EditText

    private lateinit var arrayUser: List<User>
    var user : Array<Array<String>>? = null

    lateinit var btn : Button
    var usernameData = ""
    var nameData = ""
    var passwordData = ""
    var roleData = ""

    companion object {

        fun newInstance(type : Int): LoginFragment {

            var fragment = LoginFragment()
            var args = Bundle()
            args.putInt("type",type)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var rootView = inflater!!.inflate(R.layout.fragment_login, container, false)
        initInstance(rootView)
        return rootView
    }

    fun initInstance(root: View) {
        db = context?.let { ApplicationDatabase.getInstance(it) }
        arrayUser = db?.userDao()?.getAll()!!

//        for (item in arrayUser){
//
//            Toast.makeText(context, "Test in loop", Toast.LENGTH_SHORT).show()
//
//        }


//        var storage = Storage(context!!)
        username = root.edt_username_login
        password = root.edt_password_login
        btn = root.btn_submit_login
//        user = storage.getUser()

        var type = arguments?.getInt("type")

        btn.setOnClickListener {
            var str = username.text.toString()
            var isHave = false



            if (checkNetworkConnection()){

                loginToServer()

            }else{

                loginWihtLocal()
            }
//

        }
    }


    fun loginToServer(){

        var user = username.text.toString()
        var pass = password.text.toString()

        val userAndPass= JSONObject()
        userAndPass.put("username",user)
        userAndPass.put("password",pass)

//        Toast.makeText(context, userAndPass.toString(), Toast.LENGTH_SHORT).show()

        val queue = Volley.newRequestQueue(context)
        val url = "http://139.180.142.52:3310/login"

        val stringReq = JsonObjectRequest(
            Request.Method.POST, url,userAndPass,
            Response.Listener<JSONObject> { response ->


                var tokenData = response.getJSONObject("login").getString("token")

//                var strResp = response.toString()
//                val jsonObj: JSONObject = JSONObject(strResp)
                Toast.makeText(context, tokenData, Toast.LENGTH_SHORT).show()

//                val jsonArray: JSONArray = jsonObj.getJSONArray("items")
//                var str_user: String = ""

//                for (i in 0 until jsonObj.length()) {
//                    var jsonInner: JSONObject = jsonObj.getJSONObject(i)
//                    str_user = str_user + "\n" + jsonInner.get("login")
//                }

            },
            Response.ErrorListener {
//                textView!!.text = "That didn't work!"
//                Log.d("TAG", "Connect Falied")
                Toast.makeText(context, "Connect Falied", Toast.LENGTH_SHORT).show()


            })
        queue.add(stringReq)

    }

    fun loginWihtLocal(){

        Toast.makeText(context, "Internet false", Toast.LENGTH_SHORT).show()


    }



    fun checkNetworkConnection(): Boolean {

        val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }



}