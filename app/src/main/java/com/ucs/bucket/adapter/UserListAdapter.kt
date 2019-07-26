package com.ucs.bucket

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ucs.bucket.db.db.entity.BalanceLog
import com.ucs.bucket.db.db.entity.User
import com.ucs.bucket.recyclerextensions.onClick
import kotlinx.android.synthetic.main.list_item_user.view.*

class UserListAdapter : RecyclerView.Adapter<UserViewHolder>() {



    private var arrayUser = mutableListOf<User>()
    var onItemDeleteClick: ((Int) -> Unit)? = null
    var onItemEditClick: ((Int) -> Unit)? = null

    var onItemClick: ((Int) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_user, parent, false)).onClick { view, position, type ->

            view.delete_txt.setOnClickListener {
                onItemDeleteClick?.invoke(position)
            }

            view.edit_txt.setOnClickListener {
                onItemEditClick?.invoke(position)
            }

//            view.user_adapter.setOnClickListener{
//                onItemClick?.invoke(position)
//
//
//            }
        }
    }

    override fun getItemCount(): Int = arrayUser.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bindData(arrayUser[position])
    }

    fun setUserList(arrayUser: MutableList<User>) {
        this.arrayUser = arrayUser
    }

}