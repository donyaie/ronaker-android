package com.ronaker.app.ui.searchLocationDialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterLocationSearchBinding
import com.ronaker.app.model.Place

class LocationSearchAdapter(dataList: ArrayList<Place>,viewModel: AddProductLocationSearchViewModel) : RecyclerView.Adapter<LocationSearchAdapter.ViewHolder>() {
    private  var productList:List<Place> = dataList

    private  var mViewModel: AddProductLocationSearchViewModel = viewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: AdapterLocationSearchBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_location_search, parent, false)


        return ViewHolder(
            binding,
            mViewModel
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int {
        return  productList.size
    }

    fun updateproductList(){
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: AdapterLocationSearchBinding,
       var parentViewModel: AddProductLocationSearchViewModel
    ):RecyclerView.ViewHolder(binding.root){

        private val viewModel =
            LocationSearchViewModel(
                parentViewModel.getApplication()
            )

        fun bind(product:Place){
            viewModel.bind(product,binding,parentViewModel)
            binding.viewModel = viewModel
        }
    }
}