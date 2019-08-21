package com.ucs.bucket.fragment;

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.ucs.bucket.R
import com.ucs.bucket.UserManageActivity
import com.ucs.bucket.Util.SessionManager
import kotlinx.android.synthetic.main.fragment_setting.view.*

class SettingFragment : Fragment() {
    lateinit var Btndelay : TextView
    lateinit var TvDelay : TextView
    lateinit var edtDelay : EditText
    lateinit var user : TextView


    companion object {

        fun newInstance(): SettingFragment {
            var fragment = SettingFragment()
            var args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var rootView = inflater!!.inflate(R.layout.fragment_setting, container, false)
        initInstance(rootView)
        return rootView
    }

    fun initInstance(root: View) {
        var storage = SessionManager(context!!)
        user = root.setting_user
        Btndelay = root.setting_btn_tv_delay
        TvDelay = root.setting_tv_delay
        edtDelay = root.setting_edt_delay
        TvDelay.text = "Delay ${storage.getDelay()} sec"

        user.setOnClickListener {
            context?.startActivity(Intent(context,UserManageActivity::class.java))
        }
        Btndelay.setOnClickListener {
            if(Btndelay.text=="edit"){
                Btndelay.text = "ok"
                edtDelay.visibility = View.VISIBLE
            }else{
                Btndelay.text = "edit"
                edtDelay.visibility = View.GONE
                TvDelay.text = "Delay ${edtDelay.text} sec"
                storage.setDelay(edtDelay.text.toString().toInt())

            }

        }
    }

}