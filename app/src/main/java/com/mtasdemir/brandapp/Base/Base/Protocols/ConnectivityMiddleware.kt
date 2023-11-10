package com.mtasdemir.brandapp.Base.Base.Protocols

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.mtasdemir.brandapp.Base.Base.View.BaseViewController

interface ConnectivityMiddleware {

    fun isConnectedToInternet(): Boolean {
        val connectivityManager = BaseViewController.appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

            else -> false
        }
    }
}