package com.mtasdemir.brandapp.AddMark

import com.mtasdemir.brandapp.Base.Base.BaseError
import com.mtasdemir.brandapp.Base.Base.View.BaseViewModel
import com.mtasdemir.brandapp.Service.FDBWriteService
import kotlinx.coroutines.runBlocking

interface AddMarkViewModelDelegate {
    fun addMarkRequestSuccess()
    fun addMarkRequestFailure(error: BaseError)
}


class AddMarkViewModel: BaseViewModel() {


    var delegate: AddMarkViewModelDelegate? = null

    fun sendMarkRequest(markName: String) {
        runBlocking {
            try {
                FDBWriteService.markRequest(markName = markName).update()
                delegate?.addMarkRequestSuccess()
            } catch (e: BaseError) {
                delegate?.addMarkRequestFailure(e)
            }

        }
    }


}