package com.mtasdemir.brandapp.Home

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class SplashManager {

    private val httpClient = OkHttpClient()


    fun loadImages(withCompletion: (Array<UnsplashModel>) -> Unit) {

        val url = "https://api.unsplash.com/photos".toHttpUrl().newBuilder()
            .addQueryParameter("client_id", "DkuCAlrGvvjKCpwFpNw8KbeqtVPMRp7TdDA0tY1rei4")
            .build()

        val request = Request.Builder().url(url).addHeader("Content-Type", "application/json")
            .build()

        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Error defined $e")
                println("Error defined ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.body != null) {
                    val result = Gson().fromJson(response.body!!.string(), Array<UnsplashModel>::class.java)
                    withCompletion(result)
                    println("result ${result.count()}")
                }
            }

        })
    }


}

data class UnsplashModel(
    val id: String = "",
    val urls: ImageUrls
) {

}

data class ImageUrls(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String,
    @SerializedName("small_s3")
    val smalls3: String
) {

}