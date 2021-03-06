package com.ucs.bucket

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.*
import android.net.ConnectivityManager
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.ucs.bucket.Util.SessionManager
import com.ucs.bucket.appinterface.AsyncResponseCallback
import com.ucs.bucket.db.db.ApplicationDatabase
import com.ucs.bucket.db.db.dao.UserDAO
import com.ucs.bucket.db.db.entity.User
import com.ucs.bucket.db.db.helper.RoomConstants
import com.ucs.bucket.fragment.*
import java.lang.ref.WeakReference
import android.app.job.JobScheduler
import android.app.job.JobInfo
import com.ucs.bucket.Util.ExampleJobService
import android.content.ComponentName
import android.util.Log
import java.util.concurrent.TimeUnit
import android.content.DialogInterface
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.hardware.Camera
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.FrameLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ucs.bucket.Util.SessionSerial
import com.ucs.bucket.db.db.entity.BalanceLog
import com.ucs.bucket.db.db.entity.Token
import org.json.JSONObject
import java.io.*
import java.util.*


class MainActivity : AppCompatActivity() , AsyncResponseCallback,DropMoneyFragment.OnInputSelected{



    private var db: ApplicationDatabase? = null
    private var usbService: UsbService? = null
    private var display: TextView? = null
    private var editText: EditText? = null
    private var mHandler: MyHandler? = null
    private var tvtv : TextView? = null
    private lateinit var arrayUser: List<User>
    lateinit var session : SessionManager
    lateinit var session2 : SessionSerial
    private lateinit var arrayLog: List<BalanceLog>
    private lateinit var arrayToken: List<Token>
    lateinit var vesion_app : PackageInfo


    var testId =""
    var tokentest = ""
    var user_id = 0
    var statusServer =""

    private val usbConnection = object : ServiceConnection {
        override fun onServiceConnected(arg0: ComponentName, arg1: IBinder) {
            usbService = (arg1 as UsbService.UsbBinder).service
            usbService!!.setHandler(this@MainActivity.mHandler!!)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            usbService = null
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        workManager()
        session = SessionManager(applicationContext)
        session2 = SessionSerial(applicationContext)
        mHandler = MyHandler(this)
        db = ApplicationDatabase.getInstance(this)



        if (checkNetworkConnection()){
            statusServer ="1"
            syncDataLogToServer()

            var username=intent.getStringExtra("username")
            var firstname=intent.getStringExtra("name")
            var role=intent.getStringExtra("role")
            var user_id=intent.getStringExtra("user_id")


            testId = user_id

            Log.d("Role = ",role)
            if (role.equals("O")){

                supportFragmentManager.beginTransaction()
                    .add(R.id.area_main,MainFragment.newInstance(user_id,"Onwer",username,firstname),"main")
                    .commit()

            }else if (role.equals("C")){

                supportFragmentManager.beginTransaction()
                    .add(R.id.area_main,UserFragment.newInstance(user_id,"Cashier",username,firstname),"user")
                    .commit()


            }else if (role.equals("M")){

                supportFragmentManager.beginTransaction()
                    .add(R.id.area_main,ManagerFragment.newInstance(user_id,"Manager",username,firstname),"manager")
                    .commit()

            }

        }else{

            statusServer ="2"

            var username=intent.getStringExtra("username")
            var name=intent.getStringExtra("name")
            var role=intent.getStringExtra("role")
            var user_id=intent.getStringExtra("user_id")




            if (role == "O"){

                supportFragmentManager.beginTransaction()
                    .add(R.id.area_main,MainFragment.newInstance(user_id,role,username,name),"main")
                    .commit()

            }else if (role == "C"){

                supportFragmentManager.beginTransaction()
                    .add(R.id.area_main,UserFragment.newInstance(user_id,role,username,name),"user")
                    .commit()

            }else if (role == "M"){

                supportFragmentManager.beginTransaction()
                    .add(R.id.area_main,ManagerFragment.newInstance(user_id,role,username,name),"manager")
                    .commit()
            }


        }





    }



    // Get UserID
    fun userID(): Int {

        return testId.toInt()
    }


    private val mUsbReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                UsbService.ACTION_USB_PERMISSION_GRANTED // USB PERMISSION GRANTED
                -> Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show()
                UsbService.ACTION_USB_PERMISSION_NOT_GRANTED // USB PERMISSION NOT GRANTED
                -> Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show()
                UsbService.ACTION_NO_USB // NO USB CONNECTED
                -> Toast.makeText(context, "No USB connected", Toast.LENGTH_SHORT).show()
                UsbService.ACTION_USB_DISCONNECTED // USB DISCONNECTED
                -> Toast.makeText(context, "USB disconnected", Toast.LENGTH_SHORT).show()
                UsbService.ACTION_USB_NOT_SUPPORTED // USB NOT SUPPORTED
                -> Toast.makeText(context, "USB device not supported", Toast.LENGTH_SHORT).show()
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        setFilters()  // Start listening notifications from UsbService
        startService(UsbService::class.java, usbConnection, null) // Start UsbService(if it was not started before) and Bind it

    }

    public override fun onPause() {
        super.onPause()

        unregisterReceiver(mUsbReceiver)
        unbindService(usbConnection)

//        val activityManager = applicationContext
//
//            .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//
//        activityManager.moveTaskToFront(taskId, 0)
    }

    public override fun onDestroy() {

        super.onDestroy()
//        session.logOutUser()

        logoutToserver(statusServer)

    }

    private fun startService(service: Class<*>, serviceConnection: ServiceConnection, extras: Bundle?) {
        if (!UsbService.SERVICE_CONNECTED) {
            val startService = Intent(this, service)
            if (extras != null && !extras.isEmpty) {
                val keys = extras.keySet()
                for (key in keys) {
                    val extra = extras.getString(key)
                    startService.putExtra(key, extra)
                }
            }
            startService(startService)
        }
        val bindingIntent = Intent(this, service)
        bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun setFilters() {
        val filter = IntentFilter()
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED)
        filter.addAction(UsbService.ACTION_NO_USB)
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED)
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED)
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED)
        registerReceiver(mUsbReceiver, filter)
    }


    // Recieve Data From Arduino

    fun onDataRecieve(data : String){

        var s = data.trim()
        s.replace("\r","")
        s.replace("\n","")

        if (s == "ready"){

            Toast.makeText(this, s, Toast.LENGTH_SHORT).show()


//            val fragment = supportFragmentManager.findFragmentByTag("open")
            val fragment = supportFragmentManager.findFragmentByTag("bopen")

            if(fragment!=null){

//                (fragment as OpenFragment).getData(data)

                (fragment as BeforeOpenFragment).insertKey("ready")


            }


        }else if (s.contains("S")&& s.contains("E")) {

            val fragment = supportFragmentManager.findFragmentByTag("coin")

            if(fragment!=null){

                (fragment as InsertCoinFragment).getCoin(data)
            }


        }
        else{

            Toast.makeText(this, "Data = " +  data.trim(), Toast.LENGTH_SHORT).show()


        }
    }


    // Send Data To Arduino
    fun sendData(data: String){

        Toast.makeText(this@MainActivity, data, Toast.LENGTH_SHORT).show()

        if(usbService!=null){
            usbService!!.write(data.toByteArray())
        }
    }


    // Click Logout Button
    fun closeApp(){

         val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder
        .setMessage("คุณต้องการออกจากแอพพลิเคชั่นหรือไม่ ?")
        .setCancelable(false)
        .setPositiveButton("ยืนยัน"
        ) { dialog, id ->

           logoutToserver(statusServer)

            moveTaskToBack(true)
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(1)
            finish()
        }

        .setNegativeButton("ยกเลิก", object:DialogInterface.OnClickListener {
        override fun onClick(dialog:DialogInterface, id:Int) {

            dialog.cancel()
        }
        })

                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()

                alertDialog.window.setLayout(600,220)
                alertDialog.window.attributes

                var titleText = alertDialog.findViewById<TextView>(android.R.id.message)!!
                titleText!!.setTextSize(28F)

                var noBtn = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                noBtn.setTextSize(26F)
                noBtn.setPadding(0,55,30,0)

                var yesBtn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                yesBtn.setTextSize(26F)
                yesBtn.setPadding(0,55,5,0)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        if (keyCode === KeyEvent.KEYCODE_BACK) {

//            Toast.makeText(this, "Back Button", Toast.LENGTH_SHORT).show()

        }

        return true

    }





    /*
     * This handler will be passed to UsbService. Data received from serial port is displayed through this handler
     */
    private class MyHandler(activity: MainActivity) : Handler() {
        private val mActivity: WeakReference<MainActivity>

        init {
            mActivity = WeakReference(activity)
        }

        override fun handleMessage(msg: Message) {
            when (msg.what) {
                UsbService.MESSAGE_FROM_SERIAL_PORT -> {
                    val data = msg.obj as String
                    mActivity.get()!!.onDataRecieve(data)

                }
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

    override fun onResponse(isSuccess: Boolean, call: String) {

        if (call == RoomConstants.INSERT_USER) {
            if (isSuccess) {

            } else {

            }
        }
    }




    // Function use With Add Money Drop in Deposit Fragment
    override fun sendInput(input: String) {
        val fragment = supportFragmentManager.findFragmentByTag("coin")
        (fragment as InsertCoinFragment).displayReceivedData(input)

    }

    fun getVersionApp(): String{
        val manager = this.packageManager
        val info = manager.getPackageInfo(this.packageName, PackageManager.GET_ACTIVITIES)
        return info.versionName
    }

    // Sync Data Log To Server When Internet Connect
     fun syncDataLogToServer(){

        Log.d("Sync Data = ", "Waiting")
        db = ApplicationDatabase.getInstance(this)
        arrayLog = db?.balanceLogDao()?.getLogOffline()!!



        if (arrayLog.isEmpty()){


            Log.d("Data = ","Array is Null")

        }else{

            for (item in arrayLog){




                var user_id = item.bid!!
                var username = item.username!!.toString()
                var deposit = item.deposit!!.toString()
                var drop = item.drop!!.toString()
                var balanceTotal = item.balance!!.toString()
                var action_code = item.action!!.toString()
                var detail_deposit = item.detail_deposit
                var log_id = item.log_id!!.toString()



                if (action_code.equals("CL")){

                    android.os.Handler().postDelayed({

                            var storage = SessionSerial(applicationContext!!)

                            var serial: HashMap<String, String> = storage.getUserDetails()

                            var serial_value:String = serial.get(SessionSerial.SERIAL_ID)!!
                            var veryfy_code:String = serial.get(SessionSerial.VERIFYCODE)!!



                            val depositData = JSONObject()
                            depositData.put("serial",serial_value)
                            depositData.put("username",username)
                            depositData.put("action_code",action_code)
                            depositData.put("verification_code",veryfy_code)
                            depositData.put("detail",detail_deposit)
                            depositData.put("deposit",deposit)
                            depositData.put("drop",drop)
                            depositData.put("balance",balanceTotal)



                            val updateQueue = Volley.newRequestQueue(applicationContext)
                            val url = "http://139.180.142.52/api/save/offline"
                            val updateReq = object : JsonObjectRequest(
                                Request.Method.POST, url, depositData,
                                Response.Listener { response ->

                                    Log.e("Success","OK")
                                    var logID = response.getInt("log_id")



                                    db?.balanceLogDao()?.updateLogID(logID,user_id)!!

                                    Log.e("Update Log ID  = ","Success")


                                },
                                Response.ErrorListener { response ->

                                    Log.e("Error",response.toString())
                                }) {

                            }
                            updateQueue.add(updateReq)


                        },
                        2000
                    )

                }else{

                    var storage = SessionSerial(applicationContext!!)

                    var serial: HashMap<String, String> = storage.getUserDetails()

                    var serial_value:String = serial.get(SessionSerial.SERIAL_ID)!!
                    var veryfy_code:String = serial.get(SessionSerial.VERIFYCODE)!!



                    val depositData = JSONObject()
                    depositData.put("serial",serial_value)
                    depositData.put("username",username)
                    depositData.put("action_code",action_code)
                    depositData.put("verification_code",veryfy_code)
                    depositData.put("detail",detail_deposit)
                    depositData.put("deposit",deposit)
                    depositData.put("drop",drop)
                    depositData.put("balance",balanceTotal)



                    val updateQueue = Volley.newRequestQueue(applicationContext)
                    val url = "http://139.180.142.52/api/save/offline"
                    val updateReq = object : JsonObjectRequest(
                        Request.Method.POST, url, depositData,
                        Response.Listener { response ->

                            Log.e("Success","OK")
                            var logID = response.getInt("log_id")



                            db?.balanceLogDao()?.updateLogID(logID,user_id)!!

                            Log.e("Update Log ID  = ","Success")


                        },
                        Response.ErrorListener { response ->

                            Log.e("Error",response.toString())
                        }) {

                    }
                    updateQueue.add(updateReq)
                }







            }


            Log.d("Sync Data = ", "Complete")

        }


    }


    // Logout From Server
    fun logoutToserver(serverStatus: String){

        if (serverStatus.equals("1")){

            db = ApplicationDatabase.getInstance(this)

            arrayToken = db?.tokenDao()?.getToken(testId.toInt())!!


            for (item in arrayToken){

                user_id = item.user_id!!
                tokentest = item.token!!


            }

            Log.d("user_id = ",user_id.toString())
            Log.d("token = ",tokentest)

            val updateQueue = Volley.newRequestQueue(applicationContext)
            val url = "http://139.180.142.52/api/logout"
            val updateReq = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener { response ->

                    Log.d("Logout Status Code = ",response.toString())


                    db = ApplicationDatabase.getInstance(this)
                    db?.tokenDao()?.deleteToken()!!

                    Log.d("DeleteToken = ","Success")



                },
                Response.ErrorListener { response ->

                    Log.e("Error",response.toString())
                }) {

                override fun getHeaders(): MutableMap<String, String> {

                    val header = mutableMapOf<String, String>()
                    // "Cookie" and "PHPSESSID=" + <session value> are default format
                    header.put("Accept", "application/json")
                    header.put("Authorization", "Bearer "+ tokentest)
                    return header
                }

            }
            updateQueue.add(updateReq)

        }else{


            Log.d("Server Offline = ","False")



        }

            Log.d("ไมทัน = ","")


    }

    // Check Internet
    fun checkNetworkConnection(): Boolean {

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }






}
