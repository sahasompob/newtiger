package com.ucs.bucket

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Button
import com.ucs.bucket.db.db.ApplicationDatabase
import com.ucs.bucket.db.db.entity.BalanceLog
import com.ucs.bucket.db.db.entity.OpenConsole
import kotlinx.android.synthetic.main.activity_user_list.*

class ReportOpenActivity : AppCompatActivity() {

    private lateinit var openListAdapter: OpenListAdapter
    private var db: ApplicationDatabase? = null
    private lateinit var arrayUser: List<OpenConsole>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_open)
        initView()

    }


    private fun initView() {

        db = ApplicationDatabase.getInstance(this)

        recyclerUserList.layoutManager = LinearLayoutManager(this)
        openListAdapter = OpenListAdapter()

//        balanceListAdapter.onItemDeleteClick = { position ->
//            BalanceListActivity.DeleteUserAsync(db!!.balanceLogDao(), RoomConstants.DELETE_USER, this).execute(arrayUser[position])
//        }

        arrayUser = db?.openDao()?.getAll()!!
        openListAdapter.setUserList(arrayUser.toMutableList())
        recyclerUserList.adapter = openListAdapter


        val button = findViewById<Button>(R.id.back_btn)
        button.setOnClickListener {

            //            supportFragmentManager.popBackStack()

        }
    }

}
