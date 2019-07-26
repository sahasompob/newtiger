package com.ucs.bucket.fragment;

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.ucs.bucket.R
import com.ucs.bucket.Storage
import com.ucs.bucket.db.db.ApplicationDatabase
import com.ucs.bucket.db.db.entity.User
import kotlinx.android.synthetic.main.fragment_login.view.*

class LoginFragment : Fragment() {
    private var db: ApplicationDatabase? = null

    lateinit var username : EditText
    lateinit var password : EditText

    private lateinit var arrayUser: List<User>
    var user : Array<Array<String>>? = null

    lateinit var btn : Button
    var usernameData = ""
    var nameData = ""
    var passwordData = ""
    var roleData = ""

    companion object {

        fun newInstance(type : Int): LoginFragment {

            var fragment = LoginFragment()
            var args = Bundle()
            args.putInt("type",type)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var rootView = inflater!!.inflate(R.layout.fragment_login, container, false)
        initInstance(rootView)
        return rootView
    }

    fun initInstance(root: View) {
        db = context?.let { ApplicationDatabase.getInstance(it) }
        arrayUser = db?.userDao()?.getAll()!!

//        for (item in arrayUser){
//
//            Toast.makeText(context, "Test in loop", Toast.LENGTH_SHORT).show()
//
//        }


        var storage = Storage(context!!)
        username = root.edt_username_login
        password = root.edt_password_login
        btn = root.btn_submit_login
        user = storage.getUser()

        var type = arguments?.getInt("type")

        btn.setOnClickListener {
            var str = username.text.toString()
            var isHave = false

            for (item in arrayUser){
                usernameData = item.username!!.toString()
                passwordData = item.password!!.toString()
                nameData =item.firstname!!.toString()
                roleData = item.role!!.toString()

                if (str == usernameData && password.text.toString()== passwordData){
                    val rank = roleData
                    isHave = true
                    fragmentManager?.popBackStack()

                    when(roleData){

                        "Admin" -> {

                            fragmentManager?.beginTransaction()
                                ?.replace(R.id.area_main,MainFragment.newInstance(rank,str,nameData),"main")
                                ?.addToBackStack("main")
                                ?.commit()
                        }

                        "User" -> {

                            fragmentManager?.beginTransaction()
                                ?.replace(R.id.area_main,UserFragment.newInstance(rank,str,nameData),"user")
                                ?.addToBackStack("user")
                                ?.commit()
                        }

                        "Manager" -> {

                            fragmentManager?.beginTransaction()
                                ?.replace(R.id.area_main,ManagerFragment.newInstance(rank,str,nameData),"manager")
                                ?.addToBackStack("manager")
                                ?.commit()
                        }

                        "Owner" -> {
                            fragmentManager?.beginTransaction()
                                ?.replace(R.id.area_main,MainFragment.newInstance(rank,str,nameData),"main")
                                ?.addToBackStack("setting")
                                ?.commit()

//                            if(rank=="admin"){
//
//                            }
//
//                            else username.error = "request admin"
                        }
                    }
                }
            }

//            user?.forEach {
//                if(str==it[0]&&password.text.toString()==it[1]){
//                    val rank = it[2]
//                    isHave = true
//
//                    fragmentManager?.popBackStack()
//
//                    when(type){
//
//                        1 -> {
//
//                            fragmentManager?.beginTransaction()
//                                ?.replace(R.id.area_main,MainFragment.newInstance(rank,str),"coin")
//                                ?.addToBackStack("coin")
//                                ?.commit()
//                        }
//
//                        2 -> {
//
//                        fragmentManager?.beginTransaction()
//                            ?.replace(R.id.area_main,OpenFragment.newInstance(rank,str),"open")
//                            ?.addToBackStack("open")
//                            ?.commit()
//                    }
//                        3 -> {
//                            if(rank=="admin"){
//                                fragmentManager?.beginTransaction()
//                                    ?.replace(R.id.area_main,SettingFragment.newInstance(),"setting")
//                                    ?.addToBackStack("setting")
//                                    ?.commit()
//                            }
//                            else username.error = "request admin"
//                        }
//                    }
//
//                }
//            }
//
//            if(!isHave){
//                username.error = "username/password incorrect"
//                password.error = "username/password incorrect"
//            }
        }
    }

}