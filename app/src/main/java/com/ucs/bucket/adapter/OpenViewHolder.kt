package  com.ucs.bucket

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.ucs.bucket.db.db.entity.BalanceLog
import com.ucs.bucket.db.db.entity.OpenConsole
import com.ucs.bucket.db.db.entity.User
import kotlinx.android.synthetic.main.list_item_balance.view.*
import kotlinx.android.synthetic.main.list_item_balance.view.bg_layout
import kotlinx.android.synthetic.main.list_item_open.view.*

class OpenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindData(test: OpenConsole) {


        if (adapterPosition %2 == 0){


        }else{

            itemView.bg_layout.setBackgroundColor(
                ContextCompat.getColor(itemView.context,
                R.color.recycleColor))
        }

        itemView.user.text = test.user_open
        itemView.datetime.text = test.open_time
        itemView.balance_total.text = test.balance_money.toString()
//
//        itemView.user
    }

//    fun bindData(user: User) {
//        itemView.textName.text = user.firstName + " ////" + user.lastName
//    }

}