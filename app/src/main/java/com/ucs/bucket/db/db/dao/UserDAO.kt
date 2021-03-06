package com.ucs.bucket.db.db.dao

import android.arch.persistence.room.*
import com.ucs.bucket.db.db.entity.User

/***
 * Android4Dev
 */
@Dao
interface UserDAO {

    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE username = :username")
    fun getUser(username: String): List<User>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("UPDATE user SET password=:password , action_status=:action_status WHERE uid = :user_id")
    fun updatePass(user_id: Int, password: String,action_status: Int)

    @Query("DELETE FROM user")
    fun deleteAllUser()

//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " + "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): User

    @Insert
    fun insertAll(vararg users: User)

    @Update
    fun updateUsers(vararg users: User)

    @Delete
    fun delete(user: User)
}