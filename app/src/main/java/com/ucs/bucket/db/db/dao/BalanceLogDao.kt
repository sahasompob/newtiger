package com.ucs.bucket.db.db.dao

import android.arch.persistence.room.*
import com.ucs.bucket.db.db.entity.BalanceLog
import com.ucs.bucket.db.db.entity.User

/***
 * Android4Dev
 */
@Dao
interface BalanceLogDao {

    @Query("SELECT * FROM balance_log WHERE dated = (:date)")
    fun getAll(date: String): List<BalanceLog>

    @Query("SELECT * FROM balance_log WHERE open_id = 0")
    fun getDeposit(): List<BalanceLog>

    @Query("SELECT * FROM balance_log ORDER BY bid DESC LIMIT 1")
    fun getLastId(): List<BalanceLog>

    @Query("SELECT * FROM balance_log WHERE status = 'N'")
    fun getByStatus(): List<BalanceLog>

    @Query("SELECT * FROM balance_log WHERE open_id = 0")
    fun getBeforeOpen(): List<BalanceLog>

    @Query("SELECT * FROM balance_log WHERE `action` IN (:actionStatus) AND `dated` = (:date)")
    fun getByAction(actionStatus: String,date: String): List<BalanceLog>

    @Query("SELECT * FROM balance_log WHERE bid IN (:balancelogIds)")
    fun loadAllByIds(balancelogIds: IntArray): List<BalanceLog>

    @Query("SELECT * FROM balance_log WHERE open_id IN (:openID)")
    fun loadByOpenId(openID: Int): List<BalanceLog>

    @Query("UPDATE balance_log SET open_id=:openID WHERE bid = :id")
    fun updateOpenId(openID: Int, id: Int)


//    @Query("SELECT * FROM balance_log WHERE `dated` BETWEEN (:date) IN (:actionStatus)")
//    fun getLogbyDate(date: String,actionStatus: String): List<BalanceLog>
//    " WHERE dated BETWEEN '" + startBound + " 00:00:00' AND '" + endBound + " 23:59:59' AND status = 'ENDED'"
//    @Query("SELECT * FROM balance_log WHERE first_name LIKE :first AND " + "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): BalanceLog

    @Insert
    fun insertAll(vararg balancelogs: BalanceLog)

    @Update
    fun updateAll(vararg balancelogs: BalanceLog)

    @Delete
    fun delete(balancelog: BalanceLog)
}