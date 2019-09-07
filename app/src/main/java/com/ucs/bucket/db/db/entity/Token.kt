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

        @ColumnInfo(name = "token")
        var token: String? = null



) {
    constructor() : this(0, "")
}
