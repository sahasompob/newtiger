package com.ucs.bucket.fragment;

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.ucs.bucket.MainActivity
import com.ucs.bucket.R
import com.ucs.bucket.UserManageActivity
import com.ucs.bucket.Util.SessionManager
import com.ucs.bucket.Util.SessionSerial
import kotlinx.android.synthetic.main.fragment_setting.view.*
import java.util.HashMap

class SettingFragment : Fragment() {
    lateinit var Btndelay : TextView
    lateinit var previousBtn : LinearLayout
    lateinit var TvDelay : TextView
    lateinit var edtDelay : EditText
    lateinit var user : TextView

    lateinit var BtndelayKey : TextView

    lateinit var TvDelayKey : TextView
    lateinit var edtDelayKey : EditText

    lateinit var versionApp : TextView



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
        previousBtn = root.previous_page
        user = root.setting_user
        Btndelay = root.setting_btn_tv_delay
        TvDelay = root.setting_tv_delay
        edtDelay = root.setting_edt_delay
        TvDelay.text = "ตั้งเวลาเปิดตู้ ${storage.getDelay()} วินาที"

        BtndelayKey = root.setting_btn_tv_delay_key
        TvDelayKey = root.setting_tv_delay_key
        edtDelayKey = root.setting_edt_delay_key
        TvDelayKey.text = "ตั้งเวลาบิดกุญแจ ${storage.getDelayInsertKey()} วินาที"

        versionApp = root.version_name_value
        var getVersion = (activity as MainActivity).getVersionApp()


        versionApp.text = getVersion


        user.setOnClickListener {
            context?.startActivity(Intent(context,UserManageActivity::class.java))
        }

        Btndelay.setOnClickListener {

            if(Btndelay.text=="แก้ไข"){
                Btndelay.text = "ตกลง"
                edtDelay.visibility = View.VISIBLE

            }else{


                Btndelay.text = "แก้ไข"
                edtDelay.visibility = View.GONE
                TvDelay.text = "ตั้งเวลาเปิดตู้ ${edtDelay.text} วินาที"

                if (edtDelay.text.toString().equals("")){
                    var oldOpenConsole = storage.getDelay()
                    storage.setDelay(oldOpenConsole)
                    TvDelay.text = "ตั้งเวลาเปิดตู้ ${oldOpenConsole} วินาที"

                }else{

                    storage.setDelay(edtDelay.text.toString().toInt())
//                    TvDelay.text = "ตั้งเวลาเปิดตู้ ${edtDelay.text} วินาที"
                }


            }



        }

        BtndelayKey.setOnClickListener {

            if(BtndelayKey.text=="แก้ไข"){
                BtndelayKey.text = "ตกลง"
                edtDelayKey.visibility = View.VISIBLE


            }else{

                BtndelayKey.text = "แก้ไข"
                edtDelayKey.visibility = View.GONE
                TvDelayKey.text = "ตั้งเวลาบิดกุญแจ ${edtDelayKey.text} วินาที"

                if (edtDelayKey.text.toString().equals("")){
                    var oldKeyTime = storage.getDelayInsertKey()
                    storage.setDelayKey(oldKeyTime)
                    TvDelayKey.text = "ตั้งเวลาบิดกุญแจ ${oldKeyTime} วินาที"

                }else{

                    storage.setDelayKey(edtDelayKey.text.toString().toInt())
//                    TvDelayKey.text = "ตั้งเวลาบิดกุญแจ ${edtDelayKey.text} วินาที"
                }


            }


        }


        previousBtn.setOnClickListener {

            fragmentManager?.popBackStack()
        }
    }

}