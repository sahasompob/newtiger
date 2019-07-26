package com.ucs.bucket.fragment;

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.ucs.bucket.HistoryItemCustomview
import com.ucs.bucket.R
import com.ucs.bucket.Storage
import kotlinx.android.synthetic.main.fragment_log.view.*

class LogFragment : Fragment() {
//    lateinit var area : LinearLayout

    companion object {
        fun newInstance(): LogFragment {
            var fragment = LogFragment()
            var args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var rootView = inflater!!.inflate(R.layout.fragment_log, container, false)
        initInstance(rootView)
        return rootView
    }

    fun initInstance(root: View) {


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