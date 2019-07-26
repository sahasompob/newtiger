package com.ucs.bucket

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ucs.bucket.db.db.entity.BalanceLog
import com.ucs.bucket.recyclerextensions.onClick
import kotlinx.android.synthetic.main.list_item_user.view.*

class BalanceListAdapter : RecyclerView.Adapter<BalanceViewHolder>() {



    private var arrayUser = mutableListOf<BalanceLog>()

    var onItemDeleteClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalanceViewHolder {
        return BalanceViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_balance, parent, false)).onClick { view, position, type ->
//            view.textDelete.setOnClickListener {
//                onItemDeleteClick?.invoke(position)
//            }
        }
    }

    override fun getItemCount(): Int = arrayUser.size

    override fun onBindViewHolder(holder: BalanceViewHolder, position: Int) {
        holder.bindData(arrayUser[position])
    }

    fun setUserList(arrayUser: MutableList<BalanceLog>) {
        this.arrayUser = arrayUser
    }

}