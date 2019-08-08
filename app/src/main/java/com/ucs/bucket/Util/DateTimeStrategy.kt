package com.ucs.bucket.Util

import java.text.SimpleDateFormat
import java.util.*

class DateTimeStrategy {
    private var locale: Locale? = null
    private var dateFormat: SimpleDateFormat? = null

    private fun DateTimeStrategy() {
        // Static class
    }

    /**
     * Set local of time for use in application.
     * @param lang Language.
     * @param reg Region.
     */
    fun setLocale(lang: String, reg: String) {
        locale = Locale(lang, reg)
        dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", locale!!)
    }

    /**
     * Sets current time format.
     * @param date date of this format.
     * @return current time format.
     */
    fun format(date: String): String {
        return dateFormat!!.format(Calendar.getInstance(locale).time)
    }

    /**
     * Returns current time.
     * @return current time.
     */
    fun getCurrentTime(): String {
        return dateFormat!!.format(Calendar.getInstance().time).toString()
    }

    /**
     * Convert the calendar format to date format for adapt in SQL.
     * @param instance calendar .
     * @return date format.
     */
    fun getSQLDateFormat(instance: Calendar): String {
        return dateFormat!!.format(instance.time).toString().substring(0, 10)
    }
}