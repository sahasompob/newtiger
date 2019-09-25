package com.ucs.bucket.db.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.PrimaryKey

/***
 * Android4Dev
 */
@Entity(tableName = "serial")
class Serial(
        //For autoincrement primary key
        @PrimaryKey(autoGenerate = true)
        var sid: Int = 0,

        @ColumnInfo(name = "serial_text")
        var serial_text: String? = null,

        @ColumnInfo(name = "verify_code")
        var verify_code: String? = null


) {
    constructor() : this(0, "","")
}
