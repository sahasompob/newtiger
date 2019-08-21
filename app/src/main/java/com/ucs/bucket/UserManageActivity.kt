package com.ucs.bucket

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_user_manage.*

class UserManageActivity : AppCompatActivity() {

    lateinit var area : LinearLayout
    lateinit var btn : Button
    var user : Array<Array<String>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_manage)
        area = area_user
        btn = user_save_manage
//        user = S(applicationContext).getUser()
        user?.forEach {
                    val v = UserItemCustom(applicationContext, null, 0, it)
                    area.addView(v)
        }
        val v = UserItemCustom(applicationContext,null,0, arrayOf("","","admin"))
        area.addView(v)

        btn.setOnClickListener {
            var array = mutableListOf<Array<String>>()
            for(i in 0 until area.childCount){
                val v = area.getChildAt(i) as UserItemCustom
                if(v.getUser()!=null)array.add(v.getUser()!!)
            }
//            Storage(applicationContext).setUser(array.toTypedArray())
        }


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
