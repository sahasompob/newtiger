package com.ucs.bucket


import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ucs.bucket.Util.SessionManager
import com.ucs.bucket.Util.SessionSerial
import com.ucs.bucket.appinterface.AsyncResponseCallback
import com.ucs.bucket.base.BaseActivity
import com.ucs.bucket.db.db.ApplicationDatabase
import com.ucs.bucket.db.db.dao.UserDAO
import com.ucs.bucket.db.db.entity.User
import com.ucs.bucket.db.db.helper.RoomConstants
import com.ucs.bucket.fragment.LoginFragment
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import org.json.JSONObject
import java.util.HashMap
import android.R.attr.password
import android.app.ProgressDialog
import com.ucs.bucket.db.db.dao.TokenDAO
import com.ucs.bucket.db.db.entity.Token
import org.json.JSONArray
import org.mindrot.jbcrypt.BCrypt






class LoginActivity : AppCompatActivity(), AsyncResponseCallback {

    override fun onResponse(isSuccess: Boolean, call: String) {
        if (call == RoomConstants.INSERT_USER) {
            if (isSuccess) {

            } else {

            }
        }
    }

    private var db: ApplicationDatabase? = null
    lateinit var session:SessionSerial
    lateinit var session2:SessionManager
    lateinit var username: EditText
    lateinit var password: EditText
    lateinit var btn_submit_login : Button
    private lateinit var arrayUser: List<User>
    var usernameData = ""
    var nameData = ""
    var passwordData = ""
    var roleData = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = ApplicationDatabase.getInstance(this)

        if (checkNetworkConnection()){


            db?.userDao()?.deleteAllUser()!!


            getNewDataUser()






        }else{



        }

        session = SessionSerial(applicationContext)
        session2 = SessionManager(applicationContext)

//        username = (EditText) findViewById(R.id.edt_username_login);

        username = findViewById(R.id.edt_username_login) as EditText
        password = findViewById(R.id.edt_password_login) as EditText
        btn_submit_login = findViewById(R.id.btn_submit_login) as Button



//        if (session.isLoggedIn()){
//
//            var i : Intent = Intent(applicationContext,MainActivity::class.java)
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(i)
//            finish()
//        }

//        var storage = SessionSerial(applicationContext)
//
//        var tokenTest:HashMap<String,String> = storage.getUserDetails()
////        var username:String = user.get(SessionManager.USERNAME)!!
////        var firstname:String = user.get(SessionManager.FIRSTNAME)!!
//        var serial_value:String = tokenTest.get(SessionSerial.SERIAL_ID)!!
//
//        Toast.makeText(this,serial_value,Toast.LENGTH_SHORT).show();

        btn_submit_login.setOnClickListener {
            // your code to perform when the user clicks on the button



            if (checkNetworkConnection()){


                var usertext:String = username.text.toString()
                var passtext:String = password.text.toString()

                if (usertext.equals("")){

                    Toast.makeText(this, "กรุณากรอก Username", Toast.LENGTH_SHORT).show()

                }else if (passtext.equals("")){


                    Toast.makeText(this, "กรุณากรอก Password", Toast.LENGTH_SHORT).show()

                }else{

                    loginToServer()
                }





            }else{

                var usertext:String = username.text.toString()
                var passtext:String = password.text.toString()



                if (usertext.equals("")){

                    Toast.makeText(this, "กรุณากรอก Username", Toast.LENGTH_SHORT).show()

                }else if (passtext.equals("")){


                    Toast.makeText(this, "กรุณากรอก Password", Toast.LENGTH_SHORT).show()

                }else{

                    loginWihtLocal()
                }




            }


        }





//        supportFragmentManager.beginTransaction()
//            .add(R.id.area_main, LoginFragment.newInstance(1),"main")
//            .commit()

    }

    fun loginToServer(){




        var serial: HashMap<String, String> = session.getUserDetails()

        var serial_value:String = serial.get(SessionSerial.SERIAL_ID)!!

        Toast.makeText(this,serial_value, Toast.LENGTH_SHORT).show()

        var user:String = username.text.toString()
        var pass:String = password.text.toString()

        Toast.makeText(this,serial_value,Toast.LENGTH_SHORT).show();
        val userAndPass= JSONObject()
        userAndPass.put("username",user)
        userAndPass.put("password",pass)
        userAndPass.put("serial",serial_value)

//        Toast.makeText(context, userAndPass.toString(), Toast.LENGTH_SHORT).show()

        val queue = Volley.newRequestQueue(this)
        val url = "http://139.180.142.52/api/login"
        val stringReq = JsonObjectRequest(
            Request.Method.POST, url,userAndPass,

            Response.Listener<JSONObject> { response ->

//                                Log.d("userEiei",response.toString())

                var success = response.getJSONObject("success")
                var token = success.getString("token")
                var user = success.getJSONObject("user")

                var username = user.getString("username")
                var usernameID = user.getInt("id")
                var firstname = user.getString("firstname")
                var lastname = user.getString("lastname")
                var email = user.getString("email")
                var role = user.getString("role")
//                var tokenData = response.getJSONObject("user").getString("token")
//
//
//
                Log.d("userEiei",usernameID.toString())
                Log.d("userEiei", username)
                Log.d("userEiei", firstname)
                Log.d("userEiei", lastname)
                Log.d("userEiei", email)
                Log.d("userEiei", role)
                Log.d("userEiei",token)

//                session2.creatLoginSession(usernameID.toString(),username,firstname,lastname,email,role,token)
                db = ApplicationDatabase.getInstance(this)
                val tokenData = Token(user_id = usernameID,username = username,firstname = firstname,lastname = lastname,email = email,role = role,token =token )
                InsertToken(db!!.tokenDao(), RoomConstants.INSERT_USER, this).execute(tokenData)


                var i : Intent = Intent(applicationContext,MainActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                i.putExtra("username",username)
                i.putExtra("name",firstname)
                i.putExtra("role",role)
                i.putExtra("user_id",usernameID.toString())
                startActivity(i)
                finish()



            },
            Response.ErrorListener {
                //                textView!!.text = "That didn't work!"
//                Log.d("TAG", "Connect Falied")
                Toast.makeText(this, "Connect Falied", Toast.LENGTH_SHORT).show()


            })
        queue.add(stringReq)

    }

    fun loginWihtLocal(){



//        creatOfflineUser()


        var usertext:String = username.text.toString()
        var passtext:String = password.text.toString()



        if (usertext.equals("")){

            Toast.makeText(this, "กรุณากรอก Username", Toast.LENGTH_SHORT).show()

        }else if (passtext.equals("")){


            Toast.makeText(this, "กรุณากรอก Password", Toast.LENGTH_SHORT).show()

        }else{

            val dialog = ProgressDialog.show(
                this@LoginActivity, "กำลังเข้าสู่ระบบ",
                "Loading. Please wait...", true
            )

            db = ApplicationDatabase.getInstance(this)
            arrayUser = db?.userDao()?.getUser(usertext)!!
//            arrayUser = db?.userDao()?.getAll()!!

            if (arrayUser.isEmpty()){

                Toast.makeText(this, "Username ไม่ถูกต้อง กรุณาตรวจสอบอีกครั้ง", Toast.LENGTH_SHORT).show()

            }else{




                for (item in arrayUser){

                    var user_id = item.uid
                    var username_value = item.username!!.toString().trim()
                    var pass_value= item.password!!.toString().trim()
                    var name_value =item.firstname!!
                    var role_value = item.role!!

                    var passcheck = BCrypt.checkpw(passtext.trim(),pass_value)

//                    Log.d("Result = ","Success")
//                    Log.d("usernameData = ",username_value)
//                    Log.d("name = ",name_value)
//                    Log.d("role = ",role_value)
//                    Log.d("UserId = ",user_id.toString())
//                    Log.d("Password = ",pass_value)
//                    Log.d("CheckPassword = ",passcheck.toString())

//             Log.d("usertext = ",usertext)

                    if (usertext == username_value && passcheck == true){

                        Log.d("Result = ","Success")
                        Log.d("usernameData = ",username_value)
                        Log.d("name = ",name_value)
                        Log.d("role = ",role_value)
                        Log.d("UserId = ",user_id.toString())


                        var i : Intent = Intent(applicationContext,MainActivity::class.java)
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        i.putExtra("username",username_value)
                        i.putExtra("name",name_value)
                        i.putExtra("role",role_value)
                        i.putExtra("user_id",user_id.toString())
                        startActivity(i)
                        finish()

//                        dialog.dismiss()


                    }

                }


            }

//            val dialog = ProgressDialog.show(
//                this@LoginActivity, "กำลังเข้าสู่ระบบ",
//                "Loading. Please wait...", true
//            )



        }

//        val dialog = ProgressDialog.show(
//            this@LoginActivity, "กำลังเข้าสู่ระบบ",
//            "Loading. Please wait...", true
//        )






    }

    fun creatOfflineUser(){

//        db = ApplicationDatabase.getInstance(this)
//        arrayUser = db?.userDao()?.getAll()!!
//
//        if (arrayUser.isEmpty()){
//
//            val user =
//                User(username =  "1234", firstname = "admin",
//                    password = "1234", role = "Admin")
//            MainActivity.InsertUserAsync(db!!.userDao(), RoomConstants.INSERT_USER, this).execute(user)
//
////            val user2 =
////                User(username =  "1111", firstname = "User",
////                    password = "1234", role = "User")
////            MainActivity.InsertUserAsync(db!!.userDao(), RoomConstants.INSERT_USER, this).execute(user2)
////
////
////            val user3 =
////                User(username =  "2222", firstname = "Manager",
////                    password = "2222", role = "Manager")
////            MainActivity.InsertUserAsync(db!!.userDao(), RoomConstants.INSERT_USER, this).execute(user3)
//
//
//        }else {
//
//            val user = User(username =  "1234",email = "gg@gmail.com", firstname = "admin",lastname = "eiei",
//                    password = "1234",enabled = 1, role = "O")
//            MainActivity.InsertUserAsync(db!!.userDao(), RoomConstants.INSERT_USER, this).execute(user)
//        }

    }

    fun getNewDataUser(){




//
//        Toast.makeText(this,serialData,Toast.LENGTH_SHORT).show();
//

        var storage = SessionSerial(applicationContext!!)

        var serialSesstion: HashMap<String, String> = storage.getUserDetails()

        var serialKey =  serialSesstion.get(SessionSerial.SERIAL_ID)!!
        var verifyCode = serialSesstion.get(SessionSerial.VERIFYCODE)!!

        val serial= JSONObject()
        serial.put("serial",serialKey)
        serial.put("verification_code",verifyCode)

        val queue = Volley.newRequestQueue(this)
        val url = "http://139.180.142.52/api/get/users"

        Toast.makeText(this,serialKey,Toast.LENGTH_SHORT).show();

        val stringReq = JsonObjectRequest(
            Request.Method.POST, url,serial,

            Response.Listener<JSONObject> { response ->

                var sts: JSONArray? = null
                sts = response.getJSONArray("success")



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
                            password = password,enabled = enabled, role = role,action_status = 1)

                    InstallActivity.InsertUserAsync(db!!.userDao(), RoomConstants.INSERT_USER, this).execute(user)





                }


            },
            Response.ErrorListener { response ->

                Log.d("ErrorListener", response.toString())
            })
        queue.add(stringReq)

    }


    fun checkNetworkConnection(): Boolean {

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }


//    class InsertUserAsync(private val userDao: UserDAO, private val call: String, private val responseAsyncResponse: AsyncResponseCallback) : AsyncTask<User, Void, User>() {
//        override fun doInBackground(vararg user: User?): User? {
//            return try {
//                userDao.insertAll(user[0]!!)
//                user[0]!!
//            } catch (ex: Exception) {
//                null
//            }
//        }
//
//        override fun onPostExecute(result: User?) {
//            super.onPostExecute(result)
//            if (result != null) {
//                responseAsyncResponse.onResponse(true, call)
//            } else {
//                responseAsyncResponse.onResponse(false, call)
//            }
//        }
//
//
//
//    }


    class InsertToken(private val tokenDAO: TokenDAO, private val call: String, private val responseAsyncResponse: AsyncResponseCallback) : AsyncTask<Token, Void, Token>() {
        override fun doInBackground(vararg token: Token?): Token? {
            return try {
                tokenDAO.insertToken(token[0]!!)
                token[0]!!
            } catch (ex: Exception) {
                null
            }
        }

        override fun onPostExecute(result: Token?) {
            super.onPostExecute(result)
            if (result != null) {
                responseAsyncResponse.onResponse(true, call)
            } else {
                responseAsyncResponse.onResponse(false, call)
            }
        }



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

