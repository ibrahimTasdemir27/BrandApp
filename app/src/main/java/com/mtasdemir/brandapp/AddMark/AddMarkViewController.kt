package com.mtasdemir.brandapp.AddMark

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.mtasdemir.brandapp.Base.Base.BaseError
import com.mtasdemir.brandapp.Base.Base.View.AlertAction
import com.mtasdemir.brandapp.Base.Base.View.BaseViewController
import com.mtasdemir.brandapp.Base.Base.View.BaseViewModel
import com.mtasdemir.brandapp.databinding.AddMarkLayoutBinding

class AddMarkViewController:
    BaseViewController(),
    AddMarkViewModelDelegate {

    private val viewModel = AddMarkViewModel()

    private val binding: AddMarkLayoutBinding by lazy {
        AddMarkLayoutBinding.inflate(layoutInflater)
    }

    override fun provideViewModel(): BaseViewModel? {
        return viewModel
    }

    lateinit var addMarkTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        bindItem()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }


    private fun bindItem() {
        viewModel.delegate = this

        addMarkTextView = binding.addMarkTextView
        binding.addMarkSendMarkButton.setOnClickListener {
            tappedSendMark()
        }
        binding.addMarkCloseButton.setOnClickListener {
            dismiss()
        }
    }

    private fun tappedSendMark() {
        val text = addMarkTextView.text.toString()
        viewModel.sendMarkRequest(text)
    }

    private fun dismiss() {
        finish()
    }





    /**
     * ViewModel Delegate
     */
    override fun addMarkRequestSuccess() {
        println("Marka ekleme başarılı")
        dismiss()

    }

    override fun addMarkRequestFailure(error: BaseError) {
        println("Marka ekleme başarısız $error")
        val okeyAction = AlertAction(title = "Tamam", completion = ({
            dismiss()
        }))

        val tryAction = AlertAction(title = "Tekrar Dene", completion = ({
            viewModel.sendMarkRequest(addMarkTextView.text.toString())
        }))

        showAlert(message = "Bir hata meydana geldi", leftActions = okeyAction, rightActions = tryAction)
    }



















    /**
     * Create
     */

    companion object {
        fun create(): Intent {
            val intent = Intent(appContext, AddMarkViewController::class.java)
            return intent
        }
    }

}