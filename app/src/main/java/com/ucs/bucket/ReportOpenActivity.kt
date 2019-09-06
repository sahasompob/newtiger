package com.ucs.bucket

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Button
import android.widget.Toast
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


        openListAdapter.onItemDeleteClick = { position ->

            var oid = arrayUser[position].oid

//            var usernamedata = arrayUser[position].username.toString()
//            var pass = arrayUser[position].password.toString()
//            var name = arrayUser[position].firstname.toString()
//            var role = arrayUser[position].role.toString()

            var i : Intent = Intent(applicationContext,DetailOpenActivity::class.java)
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            i.putExtra("openid",oid.toString())
//            i.putExtra("name",nameData)
//            i.putExtra("role",roleData)
            startActivity(i)


        }

        val button = findViewById<Button>(R.id.back_btn)
        button.setOnClickListener {

            finish()
            //            supportFragmentManager.popBackStack()

        }
    }

}
