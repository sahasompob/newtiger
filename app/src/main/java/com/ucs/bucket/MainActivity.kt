package com.ucs.bucket

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


class MainActivity : AppCompatActivity() , AsyncResponseCallback,DropMoneyFragment.OnInputSelected  {




    private var db: ApplicationDatabase? = null
    private var usbService: UsbService? = null
    private var display: TextView? = null
    private var editText: EditText? = null
    private var mHandler: MyHandler? = null
    private var tvtv : TextView? = null
    private lateinit var arrayUser: List<User>
    lateinit var session : SessionManager
//    private lateinit var arrayUser: null!!


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

        workManager()
        session = SessionManager(applicationContext)
        mHandler = MyHandler(this)
        db = ApplicationDatabase.getInstance(this)
//        arrayUser = db?.userDao()?.getAll()!!

        if (checkNetworkConnection()){

//            session.checkLogin()

            var user:HashMap<String,String> = session.getUserDetails()

            var username:String = user.get(SessionManager.USERNAME)!!
            var firstname:String = user.get(SessionManager.FIRSTNAME)!!
            var token:String = user.get(SessionManager.TOKEN)!!


            supportFragmentManager.beginTransaction()
                .add(R.id.area_main,MainFragment.newInstance("Admin",username,firstname),"main")
                .commit()

        }else{

            val username=intent.getStringExtra("username")
            val name=intent.getStringExtra("name")
            val role=intent.getStringExtra("role")


            if (role == "O"){

                supportFragmentManager.beginTransaction()
                    .add(R.id.area_main,MainFragment.newInstance(role,username,name),"main")
                    .commit()

            }else if (role == "C"){

                supportFragmentManager.beginTransaction()
                    .add(R.id.area_main,UserFragment.newInstance(role,username,name),"main")
                    .commit()

            }else if (role == "M"){

                supportFragmentManager.beginTransaction()
                    .add(R.id.area_main,ManagerFragment.newInstance(role,username,name),"main")
                    .commit()
            }


        }





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


    fun onDataRecieve(data : String){

//        val fragment = supportFragmentManager.findFragmentByTag("coin")
//
//        if(fragment!=null){
//
//            (fragment as InsertCoinFragment).getCoin(data)
//        }

        val s = data.trim()

        if (s == "ready"){

            val fragment = supportFragmentManager.findFragmentByTag("open")

            if(fragment!=null){

                (fragment as OpenFragment).getData(data)
            }

        }else {

            val fragment = supportFragmentManager.findFragmentByTag("coin")

            if(fragment!=null){

                (fragment as InsertCoinFragment).getCoin(data)
            }
        }
    }


    fun sendData(data: String){
        Toast.makeText(this@MainActivity, data, Toast.LENGTH_SHORT).show()
        if(usbService!=null){
            usbService!!.write(data.toByteArray())
        }
    }


    fun closeApp(){
       finish()
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

    fun checkNetworkConnection(): Boolean {

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }


    fun workManager() {

        val componentName = ComponentName(this, ExampleJobService::class.java)
        val info = JobInfo.Builder(123, componentName)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setPersisted(true)
            .setPeriodic(5000)
            .build()

        val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val resultCode = scheduler.schedule(info)
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d("MainActivity Test", "Job scheduled")


//            val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
//            scheduler.cancel(123)
//            Log.d("MainActivity Test", "Job cancelled")

        } else {
            Log.d("MainActivity Test", "Job scheduling failed")
        }

    }



    override fun sendInput(input: String) {
        val fragment = supportFragmentManager.findFragmentByTag("coin")
        (fragment as InsertCoinFragment).displayReceivedData(input)

    }
}
