package com.example.markaapp.Base.Base.View


import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kaopiz.kprogresshud.KProgressHUD

open class BaseViewController: AppCompatActivity(), BaseViewModelDelegate {

    private lateinit var viewModel: BaseViewModel
    private var progress: KProgressHUD? = null


    open fun provideViewModel(): BaseViewModel? {
        return null
    }


    //Intent ile gelen verileri dağıtma
    open fun createSuccessModifieElements() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContext = applicationContext
        initUI()
        createSuccessModifieElements()
        if (provideViewModel() != null) {
            viewModel = provideViewModel()!!
            viewModel.baseVMDelegate = this
            viewModel.onCreate()
        } else {
            println("viewModel is unwrap found nil")
        }

    }

    private fun initUI() {
        progress = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
    }


    //Android Screen LifeCycle


    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

















    //ViewModel Delegate
    override fun contentWillLoad() {
        progress?.show()
    }

    override fun contentDidLoad() {
        progress?.dismiss()
    }

    override fun readyForContent() {

    }

    override fun pageContentFailedToast(failure: String) {
    }

    override fun contentWillLoadWithSuccess(callback: () -> Unit) {
    }


    companion object {
        lateinit var appContext: Context
    }


}
