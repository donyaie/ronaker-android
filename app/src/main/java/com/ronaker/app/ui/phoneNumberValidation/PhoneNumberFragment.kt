package com.ronaker.app.ui.phoneNumberValidation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import com.jakewharton.rxbinding2.widget.RxTextView
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.utils.KeyboardManager
import com.ronaker.app.utils.view.IPagerFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable

@AndroidEntryPoint
class PhoneNumberFragment : BaseFragment(), IPagerFragment {


    private lateinit var binding: com.ronaker.app.databinding.FragmentPhoneNumbreBinding
    private val viewModel: PhoneNumberViewModel by activityViewModels()


    private lateinit var phoneNumberUtil: PhoneNumberUtil

    private var validNumber: Phonenumber.PhoneNumber? = null

    private var inPhoneChange = false

    var disposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_phone_numbre, container, false)

        binding.viewModel = viewModel

        binding.view = this@PhoneNumberFragment






        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        phoneNumberUtil = PhoneNumberUtil.getInstance()

        binding.phoneEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                binding.phoneLine.alpha = 1f
            else
                binding.phoneLine.alpha = .5f
        }

        binding.ccp.setOnCountryChangeListener {
            binding.phoneEditText.setText("")
            binding.phoneEditText.requestFocus()
            KeyboardManager.showSoftKeyboard(context, binding.phoneEditText)
        }


        disposable = RxTextView.textChanges(binding.phoneEditText).subscribe {

            if (it.isNotEmpty())
                showClear(true)
            else
                showClear(false)

            if (!inPhoneChange) {
                validateNumber(it.toString())
            }
        }
//
        //  .debounce(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())

        binding.phoneValidate.setOnClickListener { binding.phoneEditText.setText("") }




    }

    fun onClickNext() {
        viewModel.onClickPhoneNext(getPreCode() + getNumber())
    }

    private fun getPreCode(): String {

        return binding.ccp.selectedCountryCodeWithPlus

    }

    private fun getNumber(): String? {
        return validNumber?.let { it.nationalNumber.toString() + "" }

    }

    private fun validateNumber(phNumber: String?): Boolean {
        try {


            if (phNumber == null || phNumber.isEmpty()) {
                showValidate(false)
                return false
            }


            val isoCode = binding.ccp.selectedCountryNameCode
            var phoneNumber: Phonenumber.PhoneNumber? = null

            try {
                phoneNumber = phoneNumberUtil.parse(phNumber.toString(), isoCode)
            } catch (e: NumberParseException) {
//                DebugHelper.Log(TAG, e)
            }


            val isValid = phoneNumberUtil.isValidNumber(phoneNumber)
            if (isValid) {


                inPhoneChange = true

                try {

//                    DebugHelper.Log(
//                        TAG,
//                        "phoneNumber:" + phoneNumberUtil.formatInOriginalFormat(phoneNumber!!, isoCode)
//                    )


                    var formated = phoneNumberUtil.formatInOriginalFormat(phoneNumber, isoCode)

                    if (formated.indexOf("0") == 0) {
                        formated = formated.substring(1, formated.length)
                    }

                    binding.phoneEditText.setText(formated)
                    binding.phoneEditText.setSelection(formated.length)


                } catch (ex: Exception) {
                }

                inPhoneChange = false


                validNumber = phoneNumber
                showValidate(true)
                return true
            } else {

                validNumber = null

                showValidate(false)
                return false
            }

        } catch (ex: Exception) {
            validNumber = null

            showValidate(false)
            return false
        }

    }

    private fun showClear(show: Boolean) {
        if (show) {
            binding.phoneValidate.animate().alpha(1f).setDuration(300).start()
            binding.phoneValidate.isClickable = true

        } else {

            binding.phoneValidate.animate().alpha(0f).setDuration(300).start()
            binding.phoneValidate.isClickable = false
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

        fun newInstance(): PhoneNumberFragment {
            return PhoneNumberFragment()
        }
    }

    override fun onSelect() {

    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }
}