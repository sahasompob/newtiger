package com.ucs.bucket

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.CardView
import android.widget.Button

class ReportActivity : AppCompatActivity() {

    private lateinit var day_report : CardView
    private lateinit var deposit_report : CardView
    private lateinit var open_report : CardView
    private lateinit var back_page : CardView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)


        day_report = findViewById(R.id.day_report)
        open_report = findViewById(R.id.open_report)
        deposit_report = findViewById(R.id.deposit_report)

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

    }
}
