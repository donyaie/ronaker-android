package com.ronaker.app.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.ui.dashboard.DashboardActivity
import com.ronaker.app.ui.notificationHistory.NotificationHistoryActivity
import com.ronaker.app.ui.productSaved.ProductSavedActivity
import com.ronaker.app.ui.profileCompleteEdit.ProfileCompleteActivity
import com.ronaker.app.ui.profileEdit.ProfileEditActivity
import com.ronaker.app.ui.profilePaymentHistoryList.ProfilePaymentHistoryListActivity
import com.ronaker.app.ui.profileSetting.ProfileSettingActivity
import com.ronaker.app.ui.support.SupportDialog
import com.ronaker.app.utils.Alert
import com.ronaker.app.utils.extension.setEndDrawableRes
import dagger.hilt.android.AndroidEntryPoint
import io.branch.referral.Branch


@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

    private lateinit var binding: com.ronaker.app.databinding.FragmentProfileBinding

    private val viewModel: ProfileViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.viewModel = viewModel


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        viewModel.errorMessage.observe(viewLifecycleOwner, { errorMessage ->
            if (errorMessage != null) {
                Alert.makeTextError(this, errorMessage)
            }
        })

        viewModel.logOutAction.observe(viewLifecycleOwner, {
            Branch.getInstance().logout()
            activity?.let { it.startActivity(DashboardActivity.newInstance(it)) }
        })



        viewModel.userProfileComplete.observe(viewLifecycleOwner, {


            if (it) {
                binding.profileName.setEndDrawableRes(   R.drawable.ic_guide_success)

            } else {
                binding.profileName.setEndDrawableRes(0)
            }

        })




        binding.profileLayout.setOnClickListener {


            if (viewModel.isComplete) {
                requireActivity().startActivity(ProfileEditActivity.newInstance(requireContext()))

            } else {

                requireActivity().startActivity(ProfileCompleteActivity.newInstance(requireContext()))

            }
        }

        binding.notification.setOnClickListener {


            startActivity(NotificationHistoryActivity.newInstance(requireContext()))
        }


        binding.supportLayout.setOnClickListener {



            SupportDialog.DialogBuilder(childFragmentManager).show()


        }


        binding.faveLayout.setOnClickListener {


            activity?.let { it.startActivity(ProductSavedActivity.newInstance(it)) }

        }

        binding.settingLayout.setOnClickListener {

            activity?.let { it.startActivity(ProfileSettingActivity.newInstance(it)) }
        }

        binding.paymentLayout.setOnClickListener {

            activity?.let {
                it.startActivity(
                    ProfilePaymentHistoryListActivity.newInstance(
                        it
                    )
                )
            }
        }




    }

    override fun onStart() {
        super.onStart()
        viewModel.updateUser()
    }


    companion object {

        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }


}