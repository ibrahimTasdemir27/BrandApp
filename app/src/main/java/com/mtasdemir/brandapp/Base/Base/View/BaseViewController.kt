package com.mtasdemir.brandapp.Base.Base.View


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.kaopiz.kprogresshud.KProgressHUD
import com.saadahmedsoft.popupdialog.PopupDialog
import com.saadahmedsoft.popupdialog.Styles
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener


open class BaseViewController: AppCompatActivity(), BaseViewModelDelegate {

    private lateinit var viewModel: BaseViewModel
    private var progress: KProgressHUD? = null


    lateinit var loadingSuccessView: ConstraintLayout



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

    open fun showAlert(title: String = "", message: String,
               leftActions: AlertAction = AlertAction("Vazgeç"),
               rightActions: AlertAction = AlertAction("Tamam")) {


        PopupDialog.getInstance(this).setStyle(Styles.IOS).
        setHeading(title).setDescription(message).setNegativeButtonText(leftActions.title).
        setPositiveButtonText(rightActions.title).setCancelable(false).showDialog(object : OnDialogButtonClickListener() {

            override fun onPositiveClicked(dialog: Dialog?) {
                super.onPositiveClicked(dialog)
                rightActions.completion?.let { it() }
            }

            override fun onNegativeClicked(dialog: Dialog?) {
                super.onNegativeClicked(dialog)
                leftActions.completion?.let { it() }
            }
        })
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
        Toast.makeText(this, failure, Toast.LENGTH_LONG).show();
    }

    override fun contentWillLoadWithSuccess(callback: () -> Unit) {


    }


    companion object {
        lateinit var appContext: Context
    }


}


class AlertAction(
    val title: String,
    val completion: (() -> Unit)? = null
) {

}