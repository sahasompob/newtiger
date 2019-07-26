package com.ucs.bucket.db.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.ucs.bucket.db.db.dao.BalanceLogDao
import com.ucs.bucket.db.db.dao.UserDAO
import com.ucs.bucket.db.db.entity.BalanceLog
import com.ucs.bucket.db.db.entity.User

/***
 * Android4Dev
 */
@Database(entities = [(User::class),(BalanceLog::class)], version = 1)
abstract class ApplicationDatabase : RoomDatabase() {

    //Generate Singleton Instance

    companion object {
        private var INSTANCE: ApplicationDatabase? = null

        fun getInstance(context: Context): ApplicationDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context,
                        ApplicationDatabase::class.java, "android4dev.db").allowMainThreadQueries().build()
            }
            return INSTANCE!!
        }

    }

    abstract fun userDao(): UserDAO
    abstract fun balanceLogDao(): BalanceLogDao




}