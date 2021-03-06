package com.ucs.bucket.Util

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.ucs.bucket.LoginActivity

public class SessionSerial {

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
        val  SERIAL_ID : String = "serial_id"
        val  VERIFYCODE : String = "verify_code"
        val  BRANCHNAME : String = "branch_name"
        val  NUMBERCONSOLE : String = "number_console"


    }


    fun creatDataSession(serial_id:String,verify_code:String, branch_name:String, number_console:String){

        editor.putBoolean(IS_LOGIN,true)
        editor.putString(SERIAL_ID,serial_id)
        editor.putString(VERIFYCODE,verify_code)
        editor.putString(BRANCHNAME,branch_name)
        editor.putString(NUMBERCONSOLE,number_console)


        editor.commit()
//        Toast.makeText(con, username, Toast.LENGTH_SHORT).show()

    }

    fun checkLogin(){

        if (!this.isLoggedIn()){

            var i : Intent = Intent(con, LoginActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            con.startActivity(i)
        }


    }

    fun getUserDetails(): HashMap<String,String>{

        var data: Map<String,String> = HashMap<String,String>()

        (data as HashMap).put(SERIAL_ID,pref.getString(SERIAL_ID,null))
        (data as HashMap).put(VERIFYCODE,pref.getString(VERIFYCODE,null))
        (data as HashMap).put(BRANCHNAME,pref.getString(BRANCHNAME,null))
        (data as HashMap).put(NUMBERCONSOLE,pref.getString(NUMBERCONSOLE,null))



        return  data
    }

    fun deleteSession(){

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