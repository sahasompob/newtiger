package com.ucs.bucket


import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.ucs.bucket.appinterface.AsyncResponseCallback
import com.ucs.bucket.base.BaseActivity
import com.ucs.bucket.db.db.ApplicationDatabase
import com.ucs.bucket.db.db.dao.UserDAO
import com.ucs.bucket.db.db.entity.User
import com.ucs.bucket.db.db.helper.RoomConstants
import com.ucs.bucket.fragment.LoginFragment
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity(), View.OnClickListener, AsyncResponseCallback {

    private var db: ApplicationDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()

        supportFragmentManager.beginTransaction()
            .add(R.id.area_main, LoginFragment.newInstance(1),"main")
            .commit()

    }

    private fun initView() {
        db = ApplicationDatabase.getInstance(this)
//        buttonAdd.setOnClickListener(this)
//        buttonList.setOnClickListener(this)

    }

    override fun onClick(clickView: View?) {
//        when (clickView?.id) {
//            R.id.buttonAdd -> {
//                val user =
//                    User(firstName = editFirstName.text.toString(), lastName = editLastName.text.toString().trim())
//                InsertUserAsync(db!!.userDao(), RoomConstants.INSERT_USER, this).execute(user)
//            }
//
//            R.id.buttonList -> {
//                val intent = Intent(this, BalanceListActivity::class.java)
//                startActivity(intent)
//            }
//        }
    }


    override fun onResponse(isSuccess: Boolean, call: String) {

//        if (call == RoomConstants.INSERT_USER) {
//            if (isSuccess) {
//                editFirstName.text.clear()
//                editLastName.text.clear()
//                Toast.makeText(this@LoginActivity, "Successfully added", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this@LoginActivity, "Some error occur please try again later!!!", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }

    }

//        class InsertUserAsync(private val userDao: UserDAO, private val call: String, private val responseAsyncResponse: AsyncResponseCallback) : AsyncTask<User, Void, User>() {
//            override fun doInBackground(vararg user: User?): User? {
//                return try {
//                    userDao.insertAll(user[0]!!)
//                    user[0]!!
//                } catch (ex: Exception) {
//                    null
//                }
//            }

//    override fun onPostExecute(result: User?) {
//        super.onPostExecute(result)
//        if (result != null) {
//            responseAsyncResponse.onResponse(true, call)
//        } else {
//            responseAsyncResponse.onResponse(false, call)
//        }
//    }
}

