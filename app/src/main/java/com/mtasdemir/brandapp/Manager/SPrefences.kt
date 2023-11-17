package com.mtasdemir.brandapp.Manager

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.mtasdemir.brandapp.Base.Base.View.BaseViewController
import java.lang.Exception
import java.time.format.DateTimeFormatter

class SPrefencesManager {


    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        private val formatter = DateTimeFormatter.ofPattern("MMM dd yyyy, hh:mm:ss")

        private val SPrefences: SharedPreferences =
            BaseViewController.appContext.getSharedPreferences("Prefences", Context.MODE_PRIVATE)

        var isSearchable: Boolean = false
            get() {
                val differ = System.currentTimeMillis() - timeRestore

                println("tarihler arası fark: $differ")

                return differ < 120 * 1000

            }

        var timeRestore: Long
            get() {
                return SPrefences.getLong(SPKeys.timeRestoreAds.key, System.currentTimeMillis() - 60 * 60 * 1000)
            }
            set(value) {
                SPrefences.edit().putLong(SPKeys.timeRestoreAds.key, System.currentTimeMillis()).apply()
            }

        var redCountrys: Array<String>
            get() {
                val data = SPrefences.getString(SPKeys.redCountrys.key, Gson().toJson(arrayOf("İsrail")))
                return Gson().fromJson(data, Array<String>::class.java)
            }
            set(value) {
                SPrefences.edit().putString(SPKeys.redCountrys.key, Gson().toJson(value)).apply()
            }


        var greenCountrys: Array<String>
            get() {
                val data = SPrefences.getString(SPKeys.greenCountrys.key, Gson().toJson(arrayOf("Türkiye")))

                return Gson().fromJson(data, Array<String>::class.java)
            }
            set(value) {
                SPrefences.edit().putString(SPKeys.greenCountrys.key, Gson().toJson(value)).apply()
            }
    }


}



enum class SPKeys(val key: String) {
    timeRestoreAds("timeRestoreAds"),
    redCountrys("redCountrys"),
    greenCountrys("greenCountrys")
}