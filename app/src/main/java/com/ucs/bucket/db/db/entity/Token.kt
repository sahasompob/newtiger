package com.ucs.bucket.db.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.PrimaryKey

/***
 * Android4Dev
 */
@Entity(tableName = "token")
class Token(
        //For autoincrement primary key
        @PrimaryKey(autoGenerate = true)
        var tid: Int = 0,

        @ColumnInfo(name = "user_id")
        var user_id: Int? = 0,

        @ColumnInfo(name = "username")
        var username: String? = null,

        @ColumnInfo(name = "firstname")
        var firstname: String? = null,

        @ColumnInfo(name = "lastname")
        var lastname: String? = null,

        @ColumnInfo(name = "email")
        var email: String? = null,

        @ColumnInfo(name = "role")
        var role: String? = null,

        @ColumnInfo(name = "token")
        var token: String? = null



) {
    constructor() : this(0, 0,"")
}
