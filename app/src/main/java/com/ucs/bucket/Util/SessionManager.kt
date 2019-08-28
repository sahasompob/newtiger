package com.ucs.bucket.Util

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import com.ucs.bucket.LoginActivity
import com.ucs.bucket.R
import com.ucs.bucket.fragment.LoginFragment
import com.ucs.bucket.fragment.MainFragment

public class SessionManager {

    lateinit var  pref: SharedPreferences
    lateinit var  editor: SharedPreferences.Editor
    lateinit var  con: Context
    var PRIVATE_MODE : Int =0
    private fun getSharePre() : SharedPreferences =  con.getSharedPreferences(PRF_NAME,
        Context.MODE_PRIVATE
    )

    constructor(con: Context){

        this.con = con
        pref = con.getSharedPreferences(PRF_NAME,PRIVATE_MODE)
        editor = pref.edit()
    }

    companion object{
        val  PRF_NAME : String = "UserSession"
        val  IS_LOGIN : String = "isLoggedIn"
        val  USER_ID : String = "user_id"
        val  USERNAME : String = "username"
        val  FIRSTNAME : String = "firstname"
        val  LASTNAME : String = "lastname"
        val  EMAIL : String = "email"
        val  TOKEN : String = "token"

    }


    fun creatLoginSession(user_id:String,username:String,firstname:String,lastname:String,email:String,token:String){

        editor.putBoolean(IS_LOGIN,true)
        editor.putString(USER_ID,user_id)
        editor.putString(USERNAME,username)
        editor.putString(FIRSTNAME,firstname)
        editor.putString(LASTNAME,lastname)
        editor.putString(EMAIL,email)
        editor.putString(TOKEN,token)
        editor.commit()
//        Toast.makeText(con, username, Toast.LENGTH_SHORT).show()

    }

    fun checkLogin(){

        if (!this.isLoggedIn()){

            var i : Intent = Intent(con,LoginActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            con.startActivity(i)
        }


    }

    fun getUserDetails(): HashMap<String,String>{

        var user: Map<String,String> = HashMap<String,String>()

        (user as HashMap).put(USER_ID,pref.getString(USER_ID,null))
        (user as HashMap).put(USERNAME,pref.getString(USERNAME,null))
        (user as HashMap).put(FIRSTNAME,pref.getString(FIRSTNAME,null))
        (user as HashMap).put(LASTNAME,pref.getString(LASTNAME,null))
        (user as HashMap).put(EMAIL,pref.getString(EMAIL,null))
        (user as HashMap).put(TOKEN,pref.getString(TOKEN,null))

        return  user
    }

    fun logOutUser(){

        editor.clear()
        editor.commit()

//        var i : Intent = Intent(con,LoginActivity::class.java)
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        con.startActivity(i)
    }

    fun isLoggedIn():Boolean{

        return pref.getBoolean(IS_LOGIN,false)
    }

    fun setDelay(delay : Int){

        editor.putInt("delay",delay)
        editor.apply()
    }

    fun getDelay() : Int = getSharePre().getInt("delay",5)

}