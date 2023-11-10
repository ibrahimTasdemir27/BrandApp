package com.mtasdemir.brandapp.Base.Base

import java.lang.Exception

sealed class BaseError: Exception() {
    class defaultError: BaseError()
    class networkError: BaseError()
    class databaseError: BaseError()
    class customError(val text: String): BaseError()

    fun localizedDescription(): String {
        return when(this) {
            is defaultError -> "Bir şeyler yanlış gitti"
            is networkError -> "İnternet bağlantınızı kontrol edin"
            is databaseError -> "Db hatası"
            is customError -> ""
        }
    }
}