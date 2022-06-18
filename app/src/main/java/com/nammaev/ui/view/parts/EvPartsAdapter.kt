/*
 * *
 *  * Created by Nethaji on 11/9/21 1:38 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 11/9/21 12:38 PM
 *
 */

package com.nammaev.ui.view.parts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nammaev.data.network.api.response.Services
import com.nammaev.databinding.LayoutEvPartsItemBinding

typealias myService = (Services) -> Unit

class EvPartsAdapter(val myService: myService) :
    RecyclerView.Adapter<EvPartsAdapter.CategoryHolder>() {

    val serviceList = mutableListOf<Services>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        return CategoryHolder(
            LayoutEvPartsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = serviceList.size

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bindUi(position)
    }

    fun addServiceList(_categoryList: List<Services>) {
        serviceList.addAll(_categoryList)
        notifyDataSetChanged()
    }

    fun getSelectedServiceList() = serviceList

    inner class CategoryHolder(private val binding: LayoutEvPartsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindUi(position: Int) {
            binding.apply {
                serviceList[position].let { _service ->
                    tvPartTitle.text = _service.serviceName
                    tvCost.text = "Cost: ${_service.rate} sar"
                    if (position == 0)
                        tvDesc.text =
                            _service.description + _service.description + _service.description + _service.description + _service.description + _service.description + _service.description + _service.description + _service.description + _service.description
                    else
                        tvDesc.text = _service.description

                    ivServicePic.clipToOutline = true


                    btnBuy.setOnClickListener {
                        serviceList[position].isAdded = !serviceList[position].isAdded
                        myService.invoke(serviceList[position])
                    }
                }
            }
        }

    }

}
