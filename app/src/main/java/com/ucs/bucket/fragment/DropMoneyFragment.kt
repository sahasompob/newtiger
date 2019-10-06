package com.ucs.bucket.fragment

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.ucs.bucket.R
import kotlinx.android.synthetic.main.dialog_drop_money.view.*
import kotlinx.android.synthetic.main.fragment_system.view.*

class DropMoneyFragment : DialogFragment() {

    lateinit var drop_mn : EditText
    lateinit var cancle_btn : Button
    lateinit var done_btn : Button


    interface OnInputSelected {

        fun sendInput(input: String)
    }

//    public var mOnInputSelected: OnInputSelected? = null

    var mOnInputSelected:OnInputSelected? = null
//    companion object {
//        fun newInstance(): SystemFragment {
//            var fragment = SystemFragment()
//            var args = Bundle()
//            fragment.arguments = args
//            return fragment
//        }
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var rootView = inflater!!.inflate(R.layout.dialog_drop_money, container, false)
        initInstance(rootView)
        return rootView
    }

    fun initInstance(root: View) {

        drop_mn = root.drop_mn
        cancle_btn = root.cancle_btn
        done_btn = root.done_btn





        cancle_btn.setOnClickListener {

            dialog.dismiss()
        }


        done_btn.setOnClickListener {
            var dropMoney = drop_mn.text.toString()
            var dropMoneyTest = "0"

            if (dropMoney.equals("")){

                mOnInputSelected?.sendInput("0");


            }else{

                mOnInputSelected?.sendInput(dropMoney);
            }

            dialog.dismiss()


        }

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {

            mOnInputSelected = activity as OnInputSelected?

//            mOnInputSelected = targetFragment as OnInputSelected?

        } catch (e: ClassCastException) {
            Log.e("ErrorTest", "onAttach: ClassCastException : " + e.message)
        }

    }


//

}