package com.ucs.bucket.fragment;

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.ucs.bucket.R
import com.ucs.bucket.Storage
import com.ucs.bucket.UserListAdapter
import com.ucs.bucket.appinterface.AsyncResponseCallback
import com.ucs.bucket.db.db.ApplicationDatabase
import com.ucs.bucket.db.db.dao.UserDAO
import com.ucs.bucket.db.db.entity.User
import com.ucs.bucket.db.db.helper.RoomConstants
import kotlinx.android.synthetic.main.activity_user_list.*
import kotlinx.android.synthetic.main.fragment_management_user.*
import kotlinx.android.synthetic.main.fragment_management_user.view.*
import kotlinx.android.synthetic.main.fragment_open.view.*
import java.util.*

class ManagementUserFragment : Fragment(), AsyncResponseCallback {
    private var db: ApplicationDatabase? = null
    lateinit var adduser : Button
    lateinit var user_list : RecyclerView


    lateinit var editeuser : Button
    private lateinit var userListAdapter: UserListAdapter
    private lateinit var arrayUser: List<User>
    var firstnameData = ""
    var usernameData = ""
    var userId = 0



    companion object {

//        fun newInstance(type : String): ManagementUserFragment {
//            var fragment = ManagementUserFragment()
//            var args = Bundle()
//            args.putString("type",type)
//            fragment.arguments = args
//            return fragment
//        }
        fun newInstance(): ManagementUserFragment {
            var fragment = ManagementUserFragment()
            var args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var rootView = inflater!!.inflate(R.layout.fragment_management_user, container, false)
        initInstance(rootView)
        return rootView
    }

    fun initInstance(root: View) {
        db = context?.let { ApplicationDatabase.getInstance(it) }
        arrayUser = db?.userDao()?.getAll()!!

        user_list = root.user_list

        user_list.layoutManager = LinearLayoutManager(context)
        userListAdapter = UserListAdapter()
        userListAdapter.setUserList(arrayUser.toMutableList())

        userListAdapter.onItemDeleteClick = { position ->



            DeleteUserAsync(db!!.userDao(), RoomConstants.DELETE_USER, this).execute(arrayUser[position])

        }


        userListAdapter.onItemEditClick = { position ->

            var uid = arrayUser[position].uid
            var usernamedata = arrayUser[position].username.toString()
            var pass = arrayUser[position].password.toString()
            var name = arrayUser[position].firstname.toString()
            var role = arrayUser[position].role.toString()

            fragmentManager?.beginTransaction()
                ?.replace(R.id.area_main,EditUserFragment.newInstance(uid,usernamedata,pass,name,role),"editUser")
                ?.addToBackStack("editUser")
                ?.commit()

        }

        user_list.adapter = userListAdapter


        adduser = root.add_btn
        editeuser = root.edit_btn


        adduser.setOnClickListener {

            fragmentManager?.beginTransaction()
                ?.replace(R.id.area_main,AddUserFragment.newInstance(),"addUser")
                ?.addToBackStack("addUser")
                ?.commit()
        }


//        editeuser.setOnClickListener {
//
//            fragmentManager?.beginTransaction()
//                ?.replace(R.id.area_main,EditUserFragment.newInstance(),"editUser")
//                ?.addToBackStack("editUser")
//                ?.commit()
//        }

    }

    override fun onResponse(isSuccess: Boolean, call: String) {
        if (call == RoomConstants.DELETE_USER) {
            if (isSuccess) {
                arrayUser = db?.userDao()?.getAll()!!
                userListAdapter.setUserList(arrayUser.toMutableList())
                userListAdapter.notifyDataSetChanged()

                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()

            } else {

                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()

            }
        }
    }


    class DeleteUserAsync(private val userDao: UserDAO, private val call: String, private val responseAsyncResponse: AsyncResponseCallback) : AsyncTask<User, Void, User>() {
            override fun doInBackground(vararg user: User?): User? {
                return try {
                    userDao.delete(user[0]!!)
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

}