package com.ronaker.app.ui.orderMessage

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.model.Product
import com.ronaker.app.ui.profileCompleteEdit.ProfileCompleteActivity
import com.ronaker.app.utils.Alert
import com.ronaker.app.utils.extension.finishSafe
import com.ronaker.app.utils.extension.startActivityMakeScene
import java.util.*

class OrderMessageFragment : BaseFragment() {

    private lateinit var binding: com.ronaker.app.databinding.FragmentOrderMessageBinding
    private lateinit var viewModel: OrderMessageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_order_message, container, false)






        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(OrderMessageViewModel::class.java)
        binding.viewModel = viewModel




        binding.toolbar.actionTextClickListener = View.OnClickListener {

        }



        binding.toolbar.cancelClickListener = View.OnClickListener {

            activity?.finishSafe()
        }





        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            Alert.makeTextError(requireActivity(), errorMessage)
        })

        viewModel.next.observe(viewLifecycleOwner, Observer {
            requireActivity().finishSafe()
        })


        viewModel.goNext.observe(viewLifecycleOwner, Observer {
            requireActivity().startActivityMakeScene(
                ProfileCompleteActivity.newInstance(
                    requireContext()
                )
            )
        })


        viewModel.successMessage.observe(viewLifecycleOwner, Observer {
            succeccSend()
        })




        getProduct()?.let { viewModel.loadProduct(it, getStartDate(), getEndDate()) }




    }




    fun getProduct(): Product? {
        return arguments?.getParcelable(PRODUCT_KEY)
    }


    private fun getStartDate(): Date {


        return Date(arguments?.getLong(STARTDATE_KEY, -1) ?: -1)
    }


    private fun getEndDate(): Date {
        return Date(arguments?.getLong(ENDDATE_KEY, -1) ?: -1)
    }

    private fun succeccSend() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())

        builder.setMessage(getString(R.string.text_your_request_sent))
        builder.setPositiveButton(
            getString(android.R.string.ok)

        ) { dialog, _ ->
            dialog?.cancel()
            requireActivity().setResult(Activity.RESULT_OK)
            requireActivity().finishSafe()

//            startActivity(DashboardActivity.newInstance(this))

        }
        builder.setCancelable(false)

        builder.show()
    }


    companion object {
        var PRODUCT_KEY = "mProduct"
        var STARTDATE_KEY = "start_date"
        var ENDDATE_KEY = "end_date"


        var REQUEST_CODE = 347

        fun newBundle(
            product: Product?,
            startDate: Date?,
            endDate: Date?
        ): Bundle {
            val boundle = Bundle()

            if (endDate != null && startDate != null) {

                boundle.putParcelable(PRODUCT_KEY, product)
                boundle.putLong(STARTDATE_KEY, startDate.time)

                boundle.putLong(ENDDATE_KEY, endDate.time)
            }


            return boundle
        }
    }


}
