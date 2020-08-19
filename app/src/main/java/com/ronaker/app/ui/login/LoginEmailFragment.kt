package com.ronaker.app.ui.login

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.utils.*
import com.ronaker.app.utils.view.IPagerFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginEmailFragment : BaseFragment(), IPagerFragment {

    private lateinit var binding: com.ronaker.app.databinding.FragmentLoginEmailBinding
    private val viewModel: LoginViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_email, container, false)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        viewModel.emailError.observe(viewLifecycleOwner, { errorMessage ->
            if (errorMessage != null) binding.emailInput.showNotValidAlert() else binding.emailInput.hideAlert()
        })


        val text = getString(R.string.text_payments_terms)

        val ss = SpannableString(text)


        val privacy = "Privacy Policy"
        val privacy_lt = "Privatumo politika"


        val term = "Terms & Conditions"
        val term_lt = "Naudojimosi sąlygomis"

        if (text.contains(privacy))
            ss.setSpan(
                object : ClickableSpan() {
                    override fun onClick(p0: View) {

                        IntentManeger.openUrl(requireActivity(), PRIVACY_URL)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                    }

                },
                text.indexOf(privacy, 0, true),
                text.indexOf(privacy, 0, true) + privacy.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )



        if (text.contains(privacy_lt))
            ss.setSpan(
                object : ClickableSpan() {
                    override fun onClick(p0: View) {

                        IntentManeger.openUrl(requireActivity(), PRIVACY_URL_LT)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                    }

                },
                text.indexOf(privacy_lt, 0, true),
                text.indexOf(privacy_lt, 0, true) + privacy_lt.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )


        if (text.contains(term))
            ss.setSpan(
                object : ClickableSpan() {
                    override fun onClick(p0: View) {

                        IntentManeger.openUrl(requireActivity(), TERM_URL)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                    }

                },
                text.indexOf(term, 0, true),
                text.indexOf(term, 0, true) + term.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )


        if (text.contains(term_lt))
            ss.setSpan(
                object : ClickableSpan() {
                    override fun onClick(p0: View) {

                        IntentManeger.openUrl(requireActivity(), TERM_URL_LT)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                    }

                },
                text.indexOf(term_lt, 0, true),
                text.indexOf(term_lt, 0, true) + term_lt.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )


        binding.privacyText.text = ss
        binding.privacyText.movementMethod = LinkMovementMethod.getInstance()
        binding.privacyText.highlightColor = Color.TRANSPARENT



        binding.nextButton.isEnabled = false


        binding.privacyText.setOnClickListener {
            binding.privacy.isChecked = !binding.privacy.isChecked
        }

        binding.privacy.setOnCheckedChangeListener { _, b ->

            binding.nextButton.isEnabled = b

        }



    }


    companion object {

        fun newInstance(): LoginEmailFragment {
            return LoginEmailFragment()
        }
    }

    override fun onSelect() {


    }


}