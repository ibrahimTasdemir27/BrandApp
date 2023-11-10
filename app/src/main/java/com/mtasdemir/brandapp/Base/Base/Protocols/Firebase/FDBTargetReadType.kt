package com.mtasdemir.brandapp.Base.Base.Protocols.Firebase

import com.mtasdemir.brandapp.Base.Base.BaseError
import com.google.firebase.database.DatabaseReference
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await


enum class FDBBaseURL(val path: String) {
    brands("PRODUCTS")
}

interface FDBTargetReadType<T: Any>: FDBTargetType {
    var database: DatabaseReference
    fun provideBaseURL(): FDBBaseURL
    fun providePath(): String
    fun provideAutoID(): Boolean
}

interface FDBNetworkReadService<T: Any>: FDBTargetReadType<T>  {
}

@Throws(BaseError::class)
suspend inline fun <reified T : Any> FDBNetworkReadService<T>.execute(): T {
    if (!isConnectedToInternet()) {
        throw BaseError.networkError()
    }


    val snapshot = database.child(provideBaseURL().path + providePath()).get().await()
    println("snap $snapshot")


    if (provideAutoID()) {
        val values = snapshot.children.map {
            it.value
        }

        val jsonValue = Gson().toJson(values)
        return Gson().fromJson(jsonValue, T::class.java)
    }


    val responseModel = snapshot.getValue(T::class.java)
    if (responseModel != null) {
        return responseModel
    } else {
        throw BaseError.databaseError()
    }

}



@Throws(BaseError::class)
suspend inline fun <reified T : Any> FDBNetworkReadService<T>.executeOnlyArray(): T {
    if (!isConnectedToInternet()) {
        throw BaseError.networkError()
    }


    val snapshot = database.child(provideBaseURL().path + providePath()).get().await()
    println("response url ${provideBaseURL().path + providePath()}")
    val jsonValue = Gson().toJson(snapshot.value)
    val jsonItem = Gson().fromJson(jsonValue, T::class.java)

    if (jsonItem == null) {
        throw BaseError.databaseError()
    } else {
        return jsonItem
    }
}