package com.ucs.bucket

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Button
import android.widget.Toast
import com.ucs.bucket.appinterface.AsyncResponseCallback
import com.ucs.bucket.base.BaseActivity
import com.ucs.bucket.db.db.ApplicationDatabase
import com.ucs.bucket.db.db.dao.BalanceLogDao
import com.ucs.bucket.db.db.entity.BalanceLog
import com.ucs.bucket.db.db.helper.RoomConstants
import com.ucs.bucket.fragment.LoginFragment

import kotlinx.android.synthetic.main.activity_user_list.*

class BalanceListActivity : BaseActivity(), AsyncResponseCallback {

    private lateinit var balanceListAdapter: BalanceListAdapter
    private var db: ApplicationDatabase? = null
    private lateinit var arrayUser: List<BalanceLog>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)
        initView()
    }

    private fun initView() {

        db = ApplicationDatabase.getInstance(this)

        recyclerUserList.layoutManager = LinearLayoutManager(this)
        balanceListAdapter = BalanceListAdapter()

        balanceListAdapter.onItemDeleteClick = { position ->
            DeleteUserAsync(db!!.balanceLogDao(), RoomConstants.DELETE_USER, this).execute(arrayUser[position])
        }
        arrayUser = db?.balanceLogDao()?.getAll()!!
        balanceListAdapter.setUserList(arrayUser.toMutableList())
        recyclerUserList.adapter = balanceListAdapter

        val button = findViewById<Button>(R.id.back_btn)
        button.setOnClickListener {

//            supportFragmentManager.popBackStack()

        }
    }

    override fun onResponse(isSuccess: Boolean, call: String) {
        if (call == RoomConstants.DELETE_USER) {
            if (isSuccess) {
                arrayUser = db?.balanceLogDao()?.getAll()!!
                balanceListAdapter.setUserList(arrayUser.toMutableList())
                balanceListAdapter.notifyDataSetChanged()
                Toast.makeText(this@BalanceListActivity, "Successfully deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@BalanceListActivity, "Some error occur please try again later!!!", Toast.LENGTH_SHORT).show()
            }
        }
    }



        class DeleteUserAsync(private val balanceLogDao: BalanceLogDao, private val call: String, private val responseAsyncResponse: AsyncResponseCallback) : AsyncTask<BalanceLog, Void, BalanceLog>() {
            override fun doInBackground(vararg userlog: BalanceLog?): BalanceLog? {
                return try {
                    balanceLogDao.delete(userlog[0]!!)
                    userlog[0]!!
                } catch (ex: Exception) {
                    null
                }
            }

        override fun onPostExecute(result: BalanceLog?) {
            super.onPostExecute(result)
            if (result != null) {
                responseAsyncResponse.onResponse(true, call)
            } else {
                responseAsyncResponse.onResponse(false, call)
            }
        }

//
//        override fun onPostExecute(result: User?) {
//            super.onPostExecute(result)
//            if (result != null) {
//                responseAsyncResponse.onResponse(true, call)
//            } else {
//                responseAsyncResponse.onResponse(false, call)
//            }
//        }


    }

//    override fun onBackPressed() {
//
////        val fragment = supportFragmentManager.fragments
////        if(fragment.size==1){
////            super.onBackPressed()
////        }else{
////            supportFragmentManager.popBackStack()
////        }
//    }
}
