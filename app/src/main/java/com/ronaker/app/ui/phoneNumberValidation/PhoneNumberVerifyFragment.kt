package com.ronaker.app.ui.phoneNumberValidation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.jakewharton.rxbinding2.widget.RxTextView
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.utils.view.IPagerFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable

@AndroidEntryPoint
class PhoneNumberVerifyFragment : BaseFragment(), IPagerFragment {

    private lateinit var binding: com.ronaker.app.databinding.FragmentPhoneNumberVerifyBinding
    private val viewModel: PhoneNumberViewModel by activityViewModels()

    var disposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_phone_number_verify,
            container,
            false
        )
        binding.viewModel = viewModel



        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)




        disposable = RxTextView.textChanges(binding.pinEditText).subscribe {
            validateCode(it.toString())
        }



    }

    private fun validateCode(value: String) {


        if (value.length == 4) {
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

        fun newInstance(): PhoneNumberVerifyFragment {
            return PhoneNumberVerifyFragment()
        }
    }

    override fun onSelect() {
    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }

}