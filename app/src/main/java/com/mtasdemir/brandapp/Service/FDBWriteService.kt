package com.mtasdemir.brandapp.Service

import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.mtasdemir.brandapp.Base.Base.Protocols.Firebase.FDBNetworkWriteService
import com.mtasdemir.brandapp.Base.Base.Protocols.Firebase.FDBTargetWriteType


sealed class FDBWriteService: FDBTargetWriteType, FDBNetworkWriteService {
    data class setNewBrandItem(val newBrandItem: BrandNextModel, val counter: Int): FDBWriteService()





    override var database: DatabaseReference = Firebase.database.reference




    override fun updatingValues(): HashMap<String, Any> {
        var map = HashMap<String, Any>()




        /** Kullanıcı Oluşturma **/
        when (this) {
            is setNewBrandItem -> {
                val path = "PRODUCTSNEW/$counter"
                map[path] = newBrandItem
            }
        }




        return  map
    }

}

class BrandNextModel(
    val countryName: String,
    val productName: String,
    val sector: String,
    val brandImageUrl: String
) {

}
