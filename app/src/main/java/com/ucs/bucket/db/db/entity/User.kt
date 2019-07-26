package com.ucs.bucket.db.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.PrimaryKey

/***
 * Android4Dev
 */
@Entity(tableName = "user")
class User(
        //For autoincrement primary key
        @PrimaryKey(autoGenerate = true)
        var uid: Int = 0,

        @ColumnInfo(name = "username")
        var username: String? = null,


        @ColumnInfo(name = "firstname")
        var firstname: String? = null,

        @ColumnInfo(name = "password")
        var password: String? = null,

        @ColumnInfo(name = "role")
        var role: String? = null



) {
    constructor() : this(0, "", "")
}