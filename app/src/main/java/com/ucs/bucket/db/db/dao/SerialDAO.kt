package com.ucs.bucket.db.db.dao

import android.arch.persistence.room.*
import com.ucs.bucket.db.db.entity.Serial
import com.ucs.bucket.db.db.entity.Token
import com.ucs.bucket.db.db.entity.User

/***
 * Android4Dev
 */
@Dao
interface SerialDAO {

    @Query("SELECT * FROM serial")
    fun getAll(): List<Serial>
//
//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    fun loadAllByIds(userIds: IntArray): List<User>

//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " + "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): User

    @Insert
    fun insertSerial(vararg serial: Serial)

//    @Update
//    fun updateToken(vararg token: Token)
//
//    @Delete
//    fun delete(token: Token)
}