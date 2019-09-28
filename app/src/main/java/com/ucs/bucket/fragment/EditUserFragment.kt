package com.ucs.bucket.fragment;

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.ucs.bucket.R
import com.ucs.bucket.appinterface.AsyncResponseCallback
import com.ucs.bucket.db.db.ApplicationDatabase
import com.ucs.bucket.db.db.dao.UserDAO
import com.ucs.bucket.db.db.entity.User
import com.ucs.bucket.db.db.helper.RoomConstants
import kotlinx.android.synthetic.main.fragment_edit_user.view.*
import org.mindrot.jbcrypt.BCrypt

class EditUserFragment : Fragment(), AsyncResponseCallback {

    private var db: ApplicationDatabase? = null

    lateinit var text : TextView
    lateinit var edit_btn : Button
    lateinit var cancel_btn : Button

    lateinit var edt_role_spinner: Spinner

    var user_data = ""
    var pass_data = ""
    var new_pass_data = ""
    var name_data = ""
    var edt_role = ""

    var role_text =""

    lateinit var edt_username: EditText
    lateinit var edt_name: EditText
    lateinit var edt_old_password: EditText
    lateinit var edt_new_password: EditText

    var userId = 0;

    companion object {
        fun newInstance(uid: Int, usernamedata: String, pass: String, name: String, role: String): EditUserFragment {
            var fragment = EditUserFragment()
            var args = Bundle()

            args.putInt("userId",uid)
            args.putString("username",usernamedata)
            args.putString("password",pass)
            args.putString("firstname",name)
            args.putString("role",role)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var rootView = inflater!!.inflate(R.layout.fragment_edit_user, container, false)
        initInstance(rootView)
        return rootView
    }

    fun initInstance(root: View) {
        db = context?.let { ApplicationDatabase.getInstance(it) }

        edt_username = root.edt_username
        edt_name = root.edt_name
        edt_old_password = root.edt_password
        edt_new_password = root.edt_repassword
        edit_btn = root.edit_btn
        cancel_btn = root.cancel_btn
        edt_role_spinner = root.edt_role_spinner

        edt_role_spinner.adapter = ArrayAdapter(
            context,
            R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.role_list)
        )

        edt_role_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                edt_role = edt_role_spinner.selectedItem.toString()

                if (edt_role.equals("Admin")){

                    role_text = "O"

                }else if (edt_role.equals("User")){

                    role_text = "C"

                }else if (edt_role.equals("Manager")){
                    role_text = "M"

                }else{
                    role_text = "O"

                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                /*Do something if nothing selected*/
            }

        }

        userId = arguments?.getInt("userId")!!
        edt_username.setText(arguments?.getString("username")!!)
        edt_old_password.setText(arguments?.getString("password")!!)
        edt_name.setText(arguments?.getString("firstname")!!)


        Toast.makeText(context, userId.toString(), Toast.LENGTH_SHORT).show()


        edit_btn.setOnClickListener {

            user_data = edt_username.text.toString()
            pass_data = edt_old_password.text.toString()
            new_pass_data = edt_new_password.text.toString()
            name_data =  edt_name.text.toString()

//            var pass = BCrypt.checkpw(pass_data,passwordData)

//            if ()

            var newpassGen = BCrypt.hashpw(new_pass_data,BCrypt.gensalt())

//            val user =
//                User(uid = userId, username =  user_data, firstname = name_data,
//                    password = pass_data, role = role_text)
//
//            UpdateUserAsync(db!!.userDao(), RoomConstants.UPDATE_USER, this).execute(user)

//            fragmentManager?.popBackStack()
            Log.d("newPass = ",newpassGen.toString())

            db?.userDao()?.updatePass(userId,newpassGen,0)!!

             fragmentManager?.popBackStack()


        }

        cancel_btn.setOnClickListener {
            fragmentManager?.popBackStack()

        }

    }
    override fun onResponse(isSuccess: Boolean, call: String) {
        if (call == RoomConstants.DELETE_USER) {
            if (isSuccess) {

                Toast.makeText(context, "Edit Success", Toast.LENGTH_SHORT).show()

            } else {

                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()

            }
        }
    }
    class UpdateUserAsync(private val userDao: UserDAO, private val call: String, private val responseAsyncResponse: AsyncResponseCallback) : AsyncTask<User, Void, User>() {
        override fun doInBackground(vararg user: User?): User? {
            return try {
                userDao.updateUsers(user[0]!!)
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

}