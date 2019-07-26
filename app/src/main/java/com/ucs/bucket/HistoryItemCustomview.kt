package com.ucs.bucket;

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.item_history.view.*
import java.time.temporal.TemporalAmount


class HistoryItemCustomview @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    lateinit var user : TextView
    lateinit var day : TextView
    lateinit var amount: TextView
    init {
        LayoutInflater.from(context).inflate(R.layout.item_history, this, true)
        initInstance()
    }

    private fun initInstance() {
        user = item_history_name
        day = item_history_date
        amount = item_history_amount

    }

    fun setTextNaja(name : String,day : String,amount : String){
        user.text = name
        this.day.text = day
        this.amount.text = amount
    }
}