package com.ucs.bucket.db.db.dao

import android.arch.persistence.room.*
import com.ucs.bucket.db.db.entity.OpenConsole
import com.ucs.bucket.db.db.entity.Token
import com.ucs.bucket.db.db.entity.User

/***
 * Android4Dev
 */
@Dao
interface OpenDAO {

    @Query("SELECT * FROM open")
    fun getAll(): List<OpenConsole>

    @Query("SELECT * FROM open  WHERE open_date = (:date)")
    fun getByDate(date: String): List<OpenConsole>


    @Query("SELECT * FROM open ORDER BY oid DESC LIMIT 1")
    fun getLastedId(): List<OpenConsole>

    @Query("SELECT * FROM open WHERE oid IN (:openID)")
    fun getOpenDataById(openID: Int): List<OpenConsole>

//    @Query("SELECT * FROM balance_log ORDER BY bid DESC LIMIT 1")
//    fun getLastId(): List<BalanceLog>
//
//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    fun loadAllByIds(userIds: IntArray): List<User>

//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " + "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): User

    @Insert
    fun insertOpenConsole(vararg openConsole: OpenConsole)

//    @Insert
//    fun insertAll(vararg balancelogs: BalanceLog)

//    @Update
//    fun updateToken(vararg token: Token)
//
//    @Delete
//    fun delete(token: Token)
}