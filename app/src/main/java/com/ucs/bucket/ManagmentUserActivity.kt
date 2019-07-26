package com.ucs.bucket

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ucs.bucket.fragment.ManagementUserFragment

class ManagmentUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_managment_user)

        supportFragmentManager.beginTransaction()
            .add(R.id.area_main,ManagementUserFragment.newInstance(),"Managment")
            .commit()
    }




}
