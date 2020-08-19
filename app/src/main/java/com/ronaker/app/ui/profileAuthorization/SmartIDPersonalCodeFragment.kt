package com.ronaker.app.ui.profileAuthorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.jakewharton.rxbinding2.widget.RxTextView
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.utils.KeyboardManager
import com.ronaker.app.utils.view.IPagerFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable

@AndroidEntryPoint
class SmartIDPersonalCodeFragment : BaseFragment(), IPagerFragment {


    private lateinit var binding: com.ronaker.app.databinding.FragmentSmartidPersonalCodeBinding
    private val viewModel: ProfileAuthorizationViewModel by activityViewModels()

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

        binding.viewModel = viewModel



        binding.view = this





        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


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


    }


    fun onPersonalCodeNext() {
        viewModel.onNextPersonalCode(
            binding.ccp.selectedCountryNameCode,
            binding.codeEditText.text.toString().trim()
        )

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