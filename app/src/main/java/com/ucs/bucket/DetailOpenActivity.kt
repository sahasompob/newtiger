package com.ucs.bucket

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.ucs.bucket.db.db.ApplicationDatabase
import com.ucs.bucket.db.db.entity.BalanceLog
import kotlinx.android.synthetic.main.activity_user_list.*

class DetailOpenActivity : AppCompatActivity() {

    private lateinit var balanceListAdapter: BalanceListAdapter
    private var db: ApplicationDatabase? = null
    private lateinit var arrayUser: List<BalanceLog>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_open)

        var open_id=intent.getStringExtra("openid")

        Toast.makeText(this, open_id + "//DetailActivity", Toast.LENGTH_SHORT).show()


        var openID = open_id.toInt()

        db = ApplicationDatabase.getInstance(this)

        recyclerUserList.layoutManager = LinearLayoutManager(this)
        balanceListAdapter = BalanceListAdapter()
        arrayUser = db?.balanceLogDao()?.loadByOpenId(openID)!!
        balanceListAdapter.setUserList(arrayUser.toMutableList())
        recyclerUserList.adapter = balanceListAdapter

    }
}
