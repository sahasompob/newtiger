package  com.ucs.bucket

import android.support.v7.widget.RecyclerView
import android.view.View
import com.ucs.bucket.db.db.entity.BalanceLog
import com.ucs.bucket.db.db.entity.User
import kotlinx.android.synthetic.main.list_item_user.view.*

class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindData(test: User) {

        itemView.username_txt.text = test.username
        itemView.name_txt.text = test.firstname
        itemView.role_txt.text = test.role
    }

//    fun bindData(user: User) {
//        itemView.textName.text = user.firstName + " ////" + user.lastName
//    }

}