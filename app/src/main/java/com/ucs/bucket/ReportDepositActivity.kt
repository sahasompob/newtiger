package com.ucs.bucket

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Button
import com.ucs.bucket.appinterface.AsyncResponseCallback
import com.ucs.bucket.base.BaseActivity
import com.ucs.bucket.db.db.ApplicationDatabase
import com.ucs.bucket.db.db.entity.BalanceLog
import com.ucs.bucket.db.db.helper.RoomConstants
import kotlinx.android.synthetic.main.activity_user_list.*

class ReportDepositActivity : BaseActivity(), AsyncResponseCallback {

    override fun onResponse(isSuccess: Boolean, call: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var balanceListAdapter: BalanceListAdapter
    private var db: ApplicationDatabase? = null
    private lateinit var arrayUser: List<BalanceLog>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_deposit)
        initView()
    }

    private fun initView() {

        db = ApplicationDatabase.getInstance(this)

        recyclerUserList.layoutManager = LinearLayoutManager(this)
        balanceListAdapter = BalanceListAdapter()

//        balanceListAdapter.onItemDeleteClick = { position ->
//            BalanceListActivity.DeleteUserAsync(db!!.balanceLogDao(), RoomConstants.DELETE_USER, this).execute(arrayUser[position])
//        }

        arrayUser = db?.balanceLogDao()?.getDeposit()!!
        balanceListAdapter.setUserList(arrayUser.toMutableList())
        recyclerUserList.adapter = balanceListAdapter


        val button = findViewById<Button>(R.id.back_btn)
        button.setOnClickListener {

            //            supportFragmentManager.popBackStack()

        }
    }

}
