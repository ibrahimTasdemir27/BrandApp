package com.mtasdemir.brandapp.Service

import com.mtasdemir.brandapp.Base.Base.Protocols.Firebase.FDBBaseURL
import com.mtasdemir.brandapp.Base.Base.Protocols.Firebase.FDBNetworkReadService
import com.mtasdemir.brandapp.Home.BrandModel
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database


sealed class FDBReadService<T: Any>: FDBNetworkReadService<T> {
    data class getBrandList(val empty: String? = null): FDBReadService<Array<BrandModel>>()

    override var database: DatabaseReference = Firebase.database.reference

    override fun provideBaseURL(): FDBBaseURL {
        return when(this) {
            is getBrandList  -> FDBBaseURL.brands
        }
    }

    override fun providePath(): String {
        return when(this) {
            is getBrandList  -> ""
        }
    }

    override fun provideAutoID(): Boolean {
        return when(this) {
            is getBrandList  -> false
        }
    }

}