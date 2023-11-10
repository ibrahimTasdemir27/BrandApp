package com.mtasdemir.brandapp.Base.Base.Protocols.Firebase

import com.mtasdemir.brandapp.Base.Base.BaseError
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await
import java.lang.Exception

interface FDBTargetWriteType: FDBTargetType {
    var database: DatabaseReference
    fun updatingValues(): HashMap<String, Any>
}



interface FDBNetworkWriteService: FDBTargetWriteType {

    suspend fun updatee() {
        try {
            database.updateChildren(updatingValues()).await()
        } catch (e: Exception) {

        }
    }

    @Throws(BaseError::class)
    suspend fun update() {
        if (!isConnectedToInternet()) {
            throw BaseError.networkError()
            return
        }


        database.updateChildren(updatingValues())
    }
}
