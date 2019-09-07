package com.ucs.bucket

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ucs.bucket.Util.SessionSerial
import org.json.JSONObject
import org.json.JSONArray
import org.mindrot.jbcrypt.BCrypt
import retrofit2.adapter.rxjava2.Result.response



class InstallActivity : AppCompatActivity() {

    lateinit var session:SessionSerial


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_install)
        session = SessionSerial(applicationContext)

//        if (session.isLoggedIn()){
//
//
//
//            var i : Intent = Intent(applicationContext,LoginActivity::class.java)
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(i)
//            finish()
//        }

        sendSerial()


    }





    fun sendSerial(){
        var test = "wXHj0PovVCkXC"
        val serial= JSONObject()
        serial.put("serial",test)

        val queue = Volley.newRequestQueue(this)
        val url = "http://139.180.142.52/api/get/users"

        val stringReq = JsonObjectRequest(
                Request.Method.POST, url,serial,

                Response.Listener<JSONObject> { response ->

                    var sts: JSONArray? = null
                    sts = response.getJSONArray("success")

                    for (i in 0 until sts.length()) {

                        val jo = sts.getJSONObject(i)
                        var username = jo.getString("username")
                        var email = jo.getString("email")
                        var firstname = jo.getString("firstname")
                        var lastname = jo.getString("lastname")
                        var password = jo.getString("passwd")
                        var enabled = jo.getInt("enabled")
                        var role = jo.getString("role")


                        val pass = BCrypt.checkpw(password,BCrypt.gensalt(10))

//                        var testeiei =  BCrypt.hashpw(password, "")

//                        val jo = sts.getJSONObject(i)
//                        val cc = Category()
//                        cc.setId(jo.getInt("id"))
//                        cc.setName(jo.getString("name"))
//                        ar.add(cc)

                        Toast.makeText(this, pass.toString(), Toast.LENGTH_SHORT).show()




//                        session.creatLoginSession(test)

                    }



                },
                Response.ErrorListener { response ->

                    Log.d("ErrorListener", response.toString())
                })
        queue.add(stringReq)

    }
}
