package com.ucs.bucket

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ucs.bucket.Util.SessionSerial
import com.ucs.bucket.appinterface.AsyncResponseCallback
import com.ucs.bucket.db.db.ApplicationDatabase
import com.ucs.bucket.db.db.dao.UserDAO
import com.ucs.bucket.db.db.entity.User
import com.ucs.bucket.db.db.helper.RoomConstants
import org.json.JSONObject
import org.json.JSONArray
import org.mindrot.jbcrypt.BCrypt
import retrofit2.adapter.rxjava2.Result.response
import java.util.*


class InstallActivity : AppCompatActivity(), AsyncResponseCallback {

    private var db: ApplicationDatabase? = null
    private lateinit var arrayUser: List<User>
    lateinit var submit_btn : Button
    lateinit var serial_txt: EditText



    override fun onResponse(isSuccess: Boolean, call: String) {
        if (call == RoomConstants.INSERT_USER) {
            if (isSuccess) {

            } else {

            }
        }
    }

    lateinit var session:SessionSerial


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_install)
        session = SessionSerial(applicationContext)

        submit_btn = findViewById(R.id.btn_submit_login) as Button
        serial_txt = findViewById(R.id.edt_serial) as EditText



        if (session.isLoggedIn()){


            var i : Intent = Intent(applicationContext,LoginActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
            finish()
        }


        submit_btn.setOnClickListener {
            // your code to perform when the user clicks on the button



            sendSerial()

        }




    }





    fun sendSerial(){


        var serialData:String = serial_txt.text.toString()

//
//        Toast.makeText(this,serialData,Toast.LENGTH_SHORT).show();
//
        var test = serialData.trim()
        var verifyCode = genVerificationCode(test)
        val serial= JSONObject()
        serial.put("serial",test)
        serial.put("verification_code",verifyCode)

        val queue = Volley.newRequestQueue(this)
        val url = "http://139.180.142.52/api/get/users"
        Toast.makeText(this,test,Toast.LENGTH_SHORT).show();
        val stringReq = JsonObjectRequest(
                Request.Method.POST, url,serial,

                Response.Listener<JSONObject> { response ->

                    var sts: JSONArray? = null
                    sts = response.getJSONArray("success")

                    session.creatLoginSession(test,verifyCode)
//                    db = ApplicationDatabase.getInstance(this)
//                    arrayUser = db?.userDao()?.getAll()!!

                    db = ApplicationDatabase.getInstance(this)
//                    val user =
//                        User(username =  "1234",email = "admin@gmail.com", firstname = "AdminJA",lastname = "eiei",
//                            password = "1234",enabled = 1, role = "O")
//
//                    InstallActivity.InsertUserAsync(db!!.userDao(), RoomConstants.INSERT_USER, this).execute(user)


                    for (i in 0 until sts.length()) {

                        val jo = sts.getJSONObject(i)
                        var username = jo.getString("username")
                        var email = jo.getString("email")
                        var firstname = jo.getString("firstname")
                        var lastname = jo.getString("lastname")
                        var password = jo.getString("passwd")
                        var enabled = jo.getInt("enabled")
                        var role = jo.getString("role")


                        val user =
                            User(username =  username,email = email, firstname = firstname,lastname = lastname,
                                password = password,enabled = enabled, role = role)

                        InstallActivity.InsertUserAsync(db!!.userDao(), RoomConstants.INSERT_USER, this).execute(user)





                    }


                    var i : Intent = Intent(applicationContext,LoginActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(i)
                    finish()



                },
                Response.ErrorListener { response ->

                    Log.d("ErrorListener", response.toString())
                })
        queue.add(stringReq)

    }

    private fun genVerificationCode(serial: String): String {
        val r = Random()

        var random = r.nextInt(10)
        var code = random.toString()
        for (i in 0..2) {
            random = (random + 4) % serial.length
            code += serial[random]
        }

        return code
    }

    class InsertUserAsync(private val userDao: UserDAO, private val call: String, private val responseAsyncResponse: AsyncResponseCallback) : AsyncTask<User, Void, User>() {
        override fun doInBackground(vararg user: User?): User? {
            return try {
                userDao.insertAll(user[0]!!)
                user[0]!!
            } catch (ex: Exception) {
                null
            }
        }

        override fun onPostExecute(result: User?) {
            super.onPostExecute(result)
            if (result != null) {
                responseAsyncResponse.onResponse(true, call)
            } else {
                responseAsyncResponse.onResponse(false, call)
            }
        }



    }
}
