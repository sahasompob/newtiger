package com.ucs.bucket

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.CardView
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.ucs.bucket.Util.SessionSerial
import java.util.HashMap

class ReportActivity : AppCompatActivity() {

    private lateinit var day_report : CardView
    private lateinit var deposit_report : CardView
    private lateinit var open_report : CardView
    private lateinit var back_page : CardView
    private lateinit var online_btn : Button
    private lateinit var offline_btn : Button
    lateinit var nameBrach : TextView
    lateinit var numberConsole : TextView
    lateinit var nameUser : TextView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        var username=intent.getStringExtra("username")
        var firstname=intent.getStringExtra("name")
        var role=intent.getStringExtra("role")
        var user_id=intent.getStringExtra("user_id")

        day_report = findViewById(R.id.day_report)
        open_report = findViewById(R.id.open_report)
        back_page = findViewById(R.id.back_page)
        deposit_report = findViewById(R.id.deposit_report)
        online_btn = findViewById(R.id.status_online)
        offline_btn = findViewById(R.id.status_offline)
        nameBrach = findViewById(R.id.name_branch_rp)
        numberConsole = findViewById(R.id.number_console_value_rp)
        nameUser = findViewById(R.id.name_user_rp)



        var storage = SessionSerial(applicationContext!!)
        var serial: HashMap<String, String> = storage.getUserDetails()
        nameBrach.text =serial.get(SessionSerial.BRANCHNAME)!!
        numberConsole.text = serial.get(SessionSerial.NUMBERCONSOLE)

        nameUser.text = firstname






        if(checkNetworkConnection()){

            offline_btn.visibility = View.INVISIBLE

        }else{

            online_btn.visibility = View.INVISIBLE
        }


        deposit_report.setOnClickListener {

            val intent = Intent(this, ReportDepositActivity::class.java)
            // To pass any data to next activity
//                    intent.putExtra("keyIdentifier", value)
            // start your next activity
            startActivity(intent)

        }


        day_report.setOnClickListener {

            val intent = Intent(this, BalanceListActivity::class.java)
        // To pass any data to next activity
//                    intent.putExtra("keyIdentifier", value)
        // start your next activity
                    startActivity(intent)

        }


        open_report.setOnClickListener {


            val intent = Intent(this, ReportOpenActivity::class.java)
            // To pass any data to next activity
//                    intent.putExtra("keyIdentifier", value)
            // start your next activity
            startActivity(intent)


        }

        back_page.setOnClickListener {


            finish()

        }


    }

    fun checkNetworkConnection(): Boolean {

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
