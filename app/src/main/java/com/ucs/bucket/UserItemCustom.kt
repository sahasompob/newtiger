package com.ucs.bucket;

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.item_user.view.*


class UserItemCustom @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    data : Array<String>
) : FrameLayout(context, attrs, defStyleAttr) {
    lateinit var edtU : EditText
    lateinit var edtP : EditText
    lateinit var spinner: Spinner
    var rank = data[2]
    var u = data[0]
    var p = data[1]


    init {
        LayoutInflater.from(context).inflate(R.layout.item_user, this, true)
        initInstance()
    }

    private fun initInstance() {
        val adapterEnglish = ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, arrayOf("admin","user"))
        edtP = edt_password_manage
        edtU = edt_user_name_manage
        spinner = spinner_manage
        edtU.setText(u)
        edtP.setText(p)
        spinner.adapter = adapterEnglish
        if(rank=="user")spinner.setSelection(1)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position==0)rank = "admin" else rank = "user"
            }

        }
        edtP.addTextChangedListener(object  : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                p = s.toString()
            }

        })
        edtU.addTextChangedListener(object  : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                u = s.toString()
            }

        })
    }

    fun getUser():Array<String>?{
        if(u.isBlank()&&p.isBlank())return null
        return arrayOf(u,p,rank)
    }
}