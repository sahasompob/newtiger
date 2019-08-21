package com.ucs.bucket.fragment;

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.ucs.bucket.ManagmentUserActivity
import com.ucs.bucket.R
import com.ucs.bucket.appinterface.AsyncResponseCallback
import com.ucs.bucket.db.db.ApplicationDatabase
import com.ucs.bucket.db.db.dao.UserDAO
import com.ucs.bucket.db.db.entity.User
import com.ucs.bucket.db.db.helper.RoomConstants
import kotlinx.android.synthetic.main.fragment_add_user.*
import kotlinx.android.synthetic.main.fragment_add_user.view.*

class AddUserFragment : Fragment(), AsyncResponseCallback {
    lateinit var text: TextView

    lateinit var username: EditText
    lateinit var name: EditText
    lateinit var password: EditText
    lateinit var re_password: EditText
    private var db: ApplicationDatabase? = null



    lateinit var role_spinner: Spinner
    var role = ""
    lateinit var btnOk : Button
    lateinit var btnCancel : Button



    companion object {
        fun newInstance(): AddUserFragment {
            var fragment = AddUserFragment()
            var args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var rootView = inflater!!.inflate(R.layout.fragment_add_user, container, false)
        initInstance(rootView)



        return rootView
    }


    fun initInstance(root: View) {cancel_btn
        db = context?.let { ApplicationDatabase.getInstance(it) }

        username  = root.username
        name  = root.name
        password  = root.password
        re_password  = root.re_password

        btnOk  = root.add_btn
        btnCancel = root.cancel_btn
        role_spinner = root.role_spinner

        role_spinner.adapter = ArrayAdapter(
            context,
            R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.role_list)
        )


        role_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                role = role_spinner.selectedItem.toString()

                Toast.makeText(context, role, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                /*Do something if nothing selected*/
            }

        }



        btnOk.setOnClickListener {


            val user =
                User(username =  username.text.toString().trim(), firstname = name.text.toString().trim(),
                    password = password.text.toString().trim(), role = role)

            InsertUserAsync(db!!.userDao(), RoomConstants.INSERT_USER, this).execute(user)


            activity?.let{
                val intent = Intent (it, ManagmentUserActivity::class.java)
                it.startActivity(intent)
            }

        }

        btnCancel.setOnClickListener {
            fragmentManager?.popBackStack()

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
                Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()

            }
        }
    }
}