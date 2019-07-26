package com.ucs.bucket.base

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ucs.bucket.R

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }
}
