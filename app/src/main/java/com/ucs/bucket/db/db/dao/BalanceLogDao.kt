package com.ucs.bucket.db.db.dao

import android.arch.persistence.room.*
import com.ucs.bucket.db.db.entity.BalanceLog
import com.ucs.bucket.db.db.entity.User

/***
 * Android4Dev
 */
@Dao
interface BalanceLogDao {

    @Query("SELECT * FROM balance_log")
    fun getAll(): List<BalanceLog>

    @Query("SELECT * FROM balance_log ORDER BY bid DESC LIMIT 1")
    fun getLastId(): List<BalanceLog>

    @Query("SELECT * FROM balance_log WHERE status = 'N'")
    fun getByStatus(): List<BalanceLog>

    @Query("SELECT * FROM balance_log WHERE bid IN (:balancelogIds)")
    fun loadAllByIds(balancelogIds: IntArray): List<BalanceLog>

//    @Query("SELECT * FROM balance_log WHERE first_name LIKE :first AND " + "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): BalanceLog

    @Insert
    fun insertAll(vararg balancelogs: BalanceLog)

    @Update
    fun updateAll(vararg balancelogs: BalanceLog)

    @Delete
    fun delete(balancelog: BalanceLog)
}