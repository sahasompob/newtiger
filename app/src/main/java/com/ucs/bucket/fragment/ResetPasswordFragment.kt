package com.ucs.bucket.fragment;

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.ucs.bucket.R
import com.ucs.bucket.db.db.ApplicationDatabase
import kotlinx.android.synthetic.main.fragment_reset_pass.view.*
import org.mindrot.jbcrypt.BCrypt

class ResetPasswordFragment : Fragment() {
//    lateinit var area : LinearLayout
    private var db: ApplicationDatabase? = null
    lateinit var btn_save_pass : Button
    lateinit var edt_new_pass : EditText
var id = ""
    companion object {

        fun newInstance(id: String): ResetPasswordFragment {
            var fragment = ResetPasswordFragment()
            var args = Bundle()
            args.putString("id",id)
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var rootView = inflater!!.inflate(R.layout.fragment_reset_pass, container, false)
        initInstance(rootView)
        return rootView
    }

    fun initInstance(root: View) {
        db = context?.let { ApplicationDatabase.getInstance(it) }
        id = arguments?.getString("id")!!


        btn_save_pass = root.btn_save_pass
        edt_new_pass = root.edt_new_pass


        btn_save_pass.setOnClickListener {

            var userId = id.toInt()
            var newPass = edt_new_pass.text.toString()
            var newpassGen = BCrypt.hashpw(newPass, BCrypt.gensalt())
            db?.userDao()?.updatePass(userId,newpassGen,0)!!
            fragmentManager?.popBackStack()

        }

//        area = root.area_log
//        val storage = Storage(context!!)
//        val data = storage.getLog().split(";")
//        data.forEach {
//            if(it.isNotEmpty()&&it.isNotBlank()){
//                var st = it.split(",")
//                val v = HistoryItemCustomview(context!!)
//                v.setTextNaja(st[0],st[1],st[2])
//                area.addView(v)
//            }
//        }
    }

}