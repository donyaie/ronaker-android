package com.ronaker.app.ui.profileAuthorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxbinding2.widget.RxTextView
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.utils.KeyboardManager
import com.ronaker.app.utils.view.IPagerFragment
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_profile_payment.*
import kotlinx.android.synthetic.main.fragment_smartid_personal_code.view.*

class SmartIDPersonalCodeFragment : BaseFragment(), IPagerFragment {


    private lateinit var binding: com.ronaker.app.databinding.FragmentSmartidPersonalCodeBinding
    private lateinit var viewModel: ProfileAuthorizationViewModel

    var disposable: Disposable? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_smartid_personal_code,
            container,
            false
        )


        activity?.let {
            viewModel = ViewModelProvider(it).get(ProfileAuthorizationViewModel::class.java)
            binding.viewModel = viewModel


        }
        binding.view = this




        binding.ccp.setOnCountryChangeListener {
            KeyboardManager.showSoftKeyboard(context, binding.codeEditText)
        }

        disposable = RxTextView.textChanges(binding.codeEditText).subscribe {

            if (it.isNotEmpty())
                showClear(true)
            else
                showClear(false)

            validateNumber(it.toString())


        }


        binding.codeValidate.setOnClickListener { binding.codeEditText.setText("") }

        return binding.root
    }


    fun onPersonalCodeNext() {
        viewModel.onNextPersonalCode(binding.ccp.selectedCountryNameCode,binding.codeEditText.text.toString().trim())

    }


    private fun validateNumber(phNumber: String?): Boolean {
        try {


            if (phNumber.isNullOrBlank()) {
                showValidate(false)
                return false
            }

            val isValid = phNumber.trim().length == 11
            return if (isValid) {
                showValidate(true)
                true
            } else {
                showValidate(false)
                false
            }

        } catch (ex: Exception) {


            showValidate(false)
            return false
        }

    }


    private fun showClear(show: Boolean) {
        if (show) {
            binding.codeValidate.animate().alpha(1f).setDuration(300).start()
            binding.codeValidate.isClickable = true

        } else {

            binding.codeValidate.animate().alpha(0f).setDuration(300).start()
            binding.codeValidate.isClickable = false
        }
    }

    private fun showValidate(valid: Boolean) {
        if (valid) {
            activeNext(true)

        } else {
            activeNext(false)
        }
    }


    private fun activeNext(active: Boolean) {
        if (active) {

            binding.nextButton.isEnabled = active
        } else {

            binding.nextButton.isEnabled = active
        }
    }


    companion object {

        fun newInstance(): SmartIDPersonalCodeFragment {
            return SmartIDPersonalCodeFragment()
        }
    }

    override fun onSelect() {

    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }

}