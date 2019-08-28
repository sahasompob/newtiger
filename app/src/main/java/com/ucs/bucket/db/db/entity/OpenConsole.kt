package com.ucs.bucket.db.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.PrimaryKey

/***
 * Android4Dev
 */
@Entity(tableName = "open")
class OpenConsole(
        //For autoincrement primary key
        @PrimaryKey(autoGenerate = true)
        var oid: Int = 0,

        @ColumnInfo(name = "open_time")
        var open_time: String? = null,

        @ColumnInfo(name = "deposit_count")
        var deposit_count: Int? = null,

        @ColumnInfo(name = "balance_money")
        var balance_money: Int? = null,

        @ColumnInfo(name = "user_open")
        var user_open: String? = null



) {
    constructor() : this(0, "")
}