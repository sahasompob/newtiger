package com.ucs.bucket

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log

class Storage (context: Context){

    private val mContext = context
    private val PREFERENCE_NAME = "storage"
    private fun getEditor(): SharedPreferences.Editor = mContext.getSharedPreferences(PREFERENCE_NAME,MODE_PRIVATE).edit()
    private fun getSharePre() : SharedPreferences =  mContext.getSharedPreferences(PREFERENCE_NAME,MODE_PRIVATE)

    fun getUser() : Array<Array<String>>{
        val data = getSharePre().getString("member","admin,admin,admin")
        var array = mutableListOf<Array<String>>()
        if(data.isNullOrEmpty())return arrayOf(arrayOf())
        val temp = data.split(":")
        if(temp.isNotEmpty())
            temp.forEach {
                array.add(it.split(",").toTypedArray())
            }
        return array.toTypedArray()
    }
    fun getDelay() : Int = getSharePre().getInt("delay",60)
    fun getLog() : String = getSharePre().getString("log","")



    fun setUser(user : Array<Array<String>>){
        val edit = getEditor()
        var str = ""
        user.forEachIndexed  {index,it->
            it.forEachIndexed { i ,s ->
               if(i<it.size-1) str += "$s,"
                else{
                   str += "$s"
               }
            }
            if(index<user.size-1)
            str+=":"
        }
        str.dropLast(1)
        edit.putString("member",str)
        edit.apply()
    }

    fun setDelay(delay : Int){
        val edit = getEditor()
        edit.putInt("delay",delay)
        edit.apply()
    }

    fun setLog(log : String){
        val edit = getEditor()
        edit.putString("log",log)
        edit.apply()
    }
}