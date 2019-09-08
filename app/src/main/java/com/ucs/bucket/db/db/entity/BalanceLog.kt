package com.ucs.bucket.db.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.PrimaryKey

/***
 * Android4Dev
 */
@Entity(tableName = "balance_log")

class BalanceLog(
        //For autoincrement primary key
        @PrimaryKey(autoGenerate = true)
        var bid: Int = 0,

        @ColumnInfo(name = "username")
        var username: String? = null,


        @ColumnInfo(name = "dated")
        var dated: String? = null,

        @ColumnInfo(name = "datedtime")
        var datedtime: String? = null,

        @ColumnInfo(name = "action")
        var action: String? = null,

        //      ยอดฝาก

        @ColumnInfo(name = "deposit")
        var deposit: Int? = null,

        //      ยอดที่ฝากไม่ได้ (เงินที่ดรอป)

        @ColumnInfo(name = "drop")
        var drop: Int? = null,

        //      ยอดเงินก่อนหน้า

        @ColumnInfo(name = "total_deposit")
        var toto_deposit: Int? = null,

        //      ยอดเงินก่อนหน้า

        @ColumnInfo(name = "balance_before")
        var balance_before: Int? = null,

        //        รวม

        @ColumnInfo(name = "balance")

        var balance: Int? = null,

        @ColumnInfo(name = "status")

        var status: String? = null,

        @ColumnInfo(name = "sync")

        var sync: String? = null,

        @ColumnInfo(name = "open_id")

        var open_id: Int? = null,

        @ColumnInfo(name = "detail_deposit")

        var detail_deposit: String? = null




) {
    constructor() : this(0, "", "")
}