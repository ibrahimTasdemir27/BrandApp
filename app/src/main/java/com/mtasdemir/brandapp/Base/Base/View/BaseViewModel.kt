package com.mtasdemir.brandapp.Base.Base.View


interface BaseViewModelDelegate {

    fun contentWillLoad()
    fun contentDidLoad()
    fun readyForContent()
    fun pageContentFailedToast(failure: String)
    fun contentWillLoadWithSuccess(callback: () -> Unit)
}


open class BaseViewModel {

    var baseVMDelegate: BaseViewModelDelegate? = null

    open fun onCreate() {}
    open fun onStart() {}
    open fun onResume() {}
    open fun onPause() {}
    open fun onStop() {}
    open fun onDestroy() {}

}



open class BaseFragmentViewModel {

    var baseVMDelegate: BaseViewModelDelegate? = null

    fun onCreate() {}

    open fun onCreateView() {}


    open fun onViewCreated() {}

    open fun onStart() {}

    open fun onResume() {}

    open fun onPause() {}

    open fun onDestroy() {}

}
