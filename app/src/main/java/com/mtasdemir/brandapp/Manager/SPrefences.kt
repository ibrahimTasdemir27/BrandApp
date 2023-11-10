package com.mtasdemir.brandapp.Manager

import android.content.Context
import android.content.SharedPreferences
import com.mtasdemir.brandapp.Base.Base.View.BaseViewController
import java.time.format.DateTimeFormatter

class SPrefencesManager {


    companion object {
        private val formatter = DateTimeFormatter.ofPattern("MMM dd yyyy, hh:mm:ss")

        private val SPrefences: SharedPreferences =
            BaseViewController.appContext.getSharedPreferences("Prefences", Context.MODE_PRIVATE)


        /*
        var isSearchable: Boolean = false
            get() {
                val differ = timeRestore.atTime(LocalTime.now())
                println("tarihler arası fark: $differ")
                //val diff: Long = Date().time - timeRestore.epochSecond
                val seconds = 1000 / 10
                return seconds < 120

            }

        var timeRestore: LocalDate
            get() {

                val dateString = SPrefences.getString(SPKeys.timeRestoreAds.key, LocalDate.now().minus() .toString())!!
                return LocalDateTime.parse(dateString, formatter).toLocalDate()
            }
            set(value) {
                val dateString = LocalDate.now().format(formatter).toString()
                SPrefences.edit().putString(SPKeys.timeRestoreAds.key, dateString).apply()
            }

         */

        var isSearchable: Boolean = false
            get() {
                val differ = System.currentTimeMillis() - timeRestore

                println("tarihler arası fark: $differ")
                differ < 120 * 1000
                return true

            }

        var timeRestore: Long
            get() {
                return SPrefences.getLong(SPKeys.timeRestoreAds.key, System.currentTimeMillis() - 60 * 60 * 1000)
            }
            set(value) {
                SPrefences.edit().putLong(SPKeys.timeRestoreAds.key, System.currentTimeMillis()).apply()
            }
    }


}



enum class SPKeys(val key: String) {
    timeRestoreAds("timeRestoreAds"),
}