package com.ucs.bucket.fragment;

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.ucs.bucket.ManagmentUserActivity
import com.ucs.bucket.R
import com.ucs.bucket.BalanceListActivity
import kotlinx.android.synthetic.main.fragment_user.view.*

class UserFragment : Fragment() {
    lateinit var deposit : Button
    lateinit var openBox : Button
    lateinit var test_system : Button
    lateinit var test: Button
    lateinit var manage : Button
    lateinit var reset_pass : Button
    lateinit var setting : ImageView
    lateinit var nameUser : TextView

    var rank = ""
    var str = ""
    var nameData = ""
    companion object {

        fun newInstance(rank: String, str: String, nameData: String): UserFragment {

            var fragment = UserFragment()
            var args = Bundle()
            args.putString("rank",rank)
            args.putString("user",str)
            args.putString("name",nameData)
            fragment.arguments = args
            return fragment
        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var rootView = inflater!!.inflate(R.layout.fragment_user, container, false)
        initInstance(rootView)
        return rootView
    }



    fun initInstance(root: View) {
        test_system = root.test_system
        nameUser = root.name_user
        deposit = root.deposit_btn
        openBox = root.open_btn
        test = root.test_btn
        manage = root.manage_btn
        reset_pass = root.reset_pass_btn

        setting = root.img_setting

        rank = arguments?.getString("rank")!!
        str = arguments?.getString("user")!!
        nameData = arguments?.getString("name")!!
        nameUser.text = arguments?.getString("name")!!




        deposit.setOnClickListener {

            fragmentManager?.beginTransaction()
                ?.replace(R.id.area_main,InsertCoinFragment.newInstance(rank,str,nameData),"coin")
                ?.addToBackStack("coin")
                ?.commit()

            Toast.makeText(context, rank, Toast.LENGTH_SHORT).show()
        }

        openBox.setOnClickListener {

            fragmentManager?.beginTransaction()
                ?.replace(R.id.area_main,OpenFragment.newInstance(rank,str,nameData),"open")
                ?.addToBackStack("open")
                ?.commit()
        }

        manage.setOnClickListener {

            activity?.let{
                val intent = Intent (it, ManagmentUserActivity::class.java)
                it.startActivity(intent)
            }


        }

        reset_pass.setOnClickListener {

            fragmentManager?.beginTransaction()
                ?.replace(R.id.area_main,ResetPasswordFragment.newInstance(),"resetPass")
                ?.addToBackStack("resetPass")
                ?.commit()
        }


        test.setOnClickListener {

            activity?.let{
                val intent = Intent (it, BalanceListActivity::class.java)
                it.startActivity(intent)
            }
        }


        test_system.setOnClickListener {
                fragmentManager?.beginTransaction()
                    ?.replace(R.id.area_main,SystemFragment.newInstance(),"testSystem")
                    ?.addToBackStack("testSystem")
                    ?.commit()

        }


    }

}