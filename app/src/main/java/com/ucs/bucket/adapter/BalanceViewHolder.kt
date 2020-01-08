package  com.ucs.bucket

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.ucs.bucket.db.db.entity.BalanceLog
import com.ucs.bucket.db.db.entity.User
import kotlinx.android.synthetic.main.list_item_balance.view.*

class BalanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindData(test: BalanceLog) {


        if (adapterPosition %2 == 0){


        }else{

            itemView.bg_layout.setBackgroundColor(
                ContextCompat.getColor(itemView.context,
                    R.color.recycleColor))
        }

//

        var status = test.action

        if (status.equals("DE")){
            itemView.sid.text = test.username
            itemView.startTime.text = test.datedtime.toString()
            itemView.status.text = test.action
            itemView.deposit.text = test.deposit.toString()
            itemView.drop_money.text = test.drop.toString()
            itemView.total.text = test.toto_deposit.toString()
            itemView.total_balance.text = test.balance.toString()

        }else if (status.equals("OF")){

            itemView.sid.text = test.username
            itemView.startTime.text = test.datedtime.toString()
            itemView.status.text = test.action

            itemView.deposit.text = "-"
            itemView.drop_money.text = "-"
            itemView.total.text = "-"
            itemView.total_balance.text = test.balance.toString()

        }else if (status.equals("C")){

            itemView.sid.text = test.username
            itemView.startTime.text = test.datedtime.toString()
            itemView.status.text = test.action

            itemView.deposit.text = "-"
            itemView.drop_money.text = "-"
            itemView.total.text = "-"
            itemView.total_balance.text = test.balance.toString()



        }else{

            itemView.sid.text = test.username
            itemView.startTime.text = test.datedtime.toString()
            itemView.status.text = test.action

            itemView.deposit.text = "-"
            itemView.drop_money.text = "-"
            itemView.total.text = test.toto_deposit.toString()
            itemView.total_balance.text = test.balance.toString()

            itemView.bg_layout.setBackgroundColor(
                ContextCompat.getColor(itemView.context,
                    R.color.open))
        }

    }

//    fun bindData(user: User) {
//        itemView.textName.text = user.firstName + " ////" + user.lastName
//    }

}