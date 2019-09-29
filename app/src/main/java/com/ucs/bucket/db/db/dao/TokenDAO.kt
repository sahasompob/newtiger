package com.ucs.bucket.db.db.dao

import android.arch.persistence.room.*
import com.ucs.bucket.db.db.entity.Token
import com.ucs.bucket.db.db.entity.User

/***
 * Android4Dev
 */
@Dao
interface TokenDAO {

    @Query("SELECT * FROM token")
    fun getAll(): List<Token>

    @Query("SELECT * FROM token WHERE user_id IN (:userID)")
    fun getToken(userID: Int): List<Token>

    @Query("DELETE FROM token")
    fun deleteToken()

//
//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    fun loadAllByIds(userIds: IntArray): List<User>

//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " + "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): User

    @Insert
    fun insertToken(vararg token: Token)

//    @Update
//    fun updateToken(vararg token: Token)
//
//    @Delete
//    fun delete(token: Token)
}