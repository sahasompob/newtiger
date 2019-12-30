package com.ucs.bucket

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ucs.bucket.fragment.ManagementUserFragment

class ManagmentUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_managment_user)


        val username:String = intent.getStringExtra("username")
        val name:String = intent.getStringExtra("name")
        val role:String = intent.getStringExtra("role")
        val user_id:String = intent.getStringExtra("user_id")




        supportFragmentManager.beginTransaction()
            .add(R.id.area_main,ManagementUserFragment.newInstance(user_id,role,username,name),"Managment")
            .commit()
    }


    fun backPage(){
        finish()

    }



}
