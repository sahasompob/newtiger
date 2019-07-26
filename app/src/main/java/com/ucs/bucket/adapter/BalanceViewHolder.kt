package  com.ucs.bucket

import android.support.v7.widget.RecyclerView
import android.view.View
import com.ucs.bucket.db.db.entity.BalanceLog
import com.ucs.bucket.db.db.entity.User
import kotlinx.android.synthetic.main.list_item_balance.view.*

class BalanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindData(test: BalanceLog) {

        var status = test.action

        if (status.equals("DE")){
            itemView.sid.text = test.username
            itemView.startTime.text = test.dated
            itemView.status.text = test.action

            itemView.deposit.text = test.deposit.toString()
            itemView.drop_money.text = test.status.toString()
            itemView.total.text = test.toto_deposit.toString()
            itemView.total_balance.text = test.balance.toString()

        }else if (status.equals("OPF")){

            itemView.sid.text = test.username
            itemView.startTime.text = test.dated
            itemView.status.text = test.action

            itemView.deposit.text = "-"
            itemView.drop_money.text = "-"
            itemView.total.text = "-"
            itemView.total_balance.text = test.balance.toString()


        }else{

            itemView.sid.text = test.username
            itemView.startTime.text = test.dated
            itemView.status.text = test.action

            itemView.deposit.text = "-"
            itemView.drop_money.text = "-"
            itemView.total.text = test.toto_deposit.toString()
            itemView.total_balance.text = test.balance.toString()
        }

    }

//    fun bindData(user: User) {
//        itemView.textName.text = user.firstName + " ////" + user.lastName
//    }

}