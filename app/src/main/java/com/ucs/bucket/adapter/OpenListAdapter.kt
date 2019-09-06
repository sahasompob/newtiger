package com.ucs.bucket

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ucs.bucket.db.db.entity.BalanceLog
import com.ucs.bucket.db.db.entity.OpenConsole
import com.ucs.bucket.recyclerextensions.onClick
import kotlinx.android.synthetic.main.list_item_open.view.*
import kotlinx.android.synthetic.main.list_item_user.view.*
import kotlinx.android.synthetic.main.recycleview_user.view.*

class OpenListAdapter : RecyclerView.Adapter<OpenViewHolder>() {



    private var arrayUser = mutableListOf<OpenConsole>()

    var onItemDeleteClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpenViewHolder {
        return OpenViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_open, parent, false)).onClick { view, position, type ->

            view.bg_layout.setOnClickListener {
                onItemDeleteClick?.invoke(position)
            }
        }
    }

    override fun getItemCount(): Int = arrayUser.size

    override fun onBindViewHolder(holder: OpenViewHolder, position: Int) {
        holder.bindData(arrayUser[position])
    }

    fun setUserList(arrayUser: MutableList<OpenConsole>) {
        this.arrayUser = arrayUser
    }

}