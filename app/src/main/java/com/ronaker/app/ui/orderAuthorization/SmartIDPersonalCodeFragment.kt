package com.ronaker.app.ui.orderAuthorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.model.Order
import com.ronaker.app.utils.AppDebug
import com.ronaker.app.utils.toCurrencyFormat
import com.ronaker.app.utils.view.IPagerFragment
import io.reactivex.disposables.Disposable
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class SmartIDPersonalCodeFragment : BaseFragment(), IPagerFragment {


    private lateinit var binding: com.ronaker.app.databinding.FragmentOrderSmartidPersonalCodeBinding
    private lateinit var viewModel: OrderAuthorizationViewModel

    var disposable: Disposable? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_order_smartid_personal_code,
            container,
            false
        )


        activity?.let {
            viewModel = ViewModelProvider(it).get(OrderAuthorizationViewModel::class.java)
            binding.viewModel = viewModel
        }




        viewModel.language.observe(viewLifecycleOwner, Observer {
            viewContract(it)

        })


        return binding.root
    }


    private fun viewContract(language: String) {
        getOrder()?.let { order ->


            if (!isCanSign())
                binding.nextButton.visibility = View.GONE


            val filename = "contract-${if (language.trim().toLowerCase(Locale.ROOT)
                    .compareTo("lt") == 0
            ) "lt" else "en"}.html"

            val input: InputStream =
                requireContext().assets.open(filename)
            val size: Int = input.available()

            val buffer = ByteArray(size)
            input.read(buffer)
            input.close()

            var totalPrice = 0.0
            order.price?.forEach {
                if (Order.OrderPriceEnum[it.key] == Order.OrderPriceEnum.Total)
                    totalPrice = if (it.price == 0.0) 0.0 else it.price / 100.0
            }

            val str = String(buffer)
                .replace(
                    "[INCLUDE RENTER NAME]",
                    "${order.orderUser?.first_name} ${order.orderUser?.last_name}"
                )
                .replace(
                    "[INCLUDE LISTER NAME]",
                    "${order.productOwner?.first_name} ${order.productOwner?.last_name}"
                )
//                    .replace(
//                        "[INCLUDE SIGN DATE]",
//                        String.format(
//                            "from %s to %s",
//                            SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(order.fromDate),
//                            SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(order.toDate))
//
//                    )
                .replace(
                    "[INSERT STARTING DATE]",
                    SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag(language)).format(order.fromDate)

                ).replace(
                    "[INSERT ENDING DATE]",
                    SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag(language)).format(order.toDate)

                ).replace(
                    "[RENT FEE]",
                    totalPrice.toCurrencyFormat(null, false)

                )

            binding.webView.loadDataWithBaseURL(null, str, "text/html", "UTF-8", null)
//                binding.webView.loadData(str, "text/html", "utf-8")


        } ?: run {
            requireActivity().finish()
        }
    }


    companion object {

        fun newInstance(): SmartIDPersonalCodeFragment {
            return SmartIDPersonalCodeFragment()
        }
    }

    override fun onSelect() {

    }


    private fun isCanSign(): Boolean {
        if (requireActivity().intent.hasExtra(OrderAuthorizationActivity.CANSIGN_KEY)) {

            return requireActivity().intent.getBooleanExtra(
                OrderAuthorizationActivity.CANSIGN_KEY,
                true
            )

        }
        return true
    }


    private fun getOrder(): Order? {
        if (requireActivity().intent.hasExtra(OrderAuthorizationActivity.Order_KEY)) {

            return requireActivity().intent.getParcelableExtra<Order?>(OrderAuthorizationActivity.Order_KEY)

        }
        return null
    }


    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }


}