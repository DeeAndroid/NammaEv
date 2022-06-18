/*
 * *
 *  * Created by Nethaji on 11/9/21 1:38 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 11/9/21 12:38 PM
 *
 */

package com.nammaev.ui.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.nammaev.R
import com.nammaev.data.network.api.response.Services
import com.nammaev.databinding.LayoutUserItemBinding
import com.nammaev.di.getCompatColor
import com.nammaev.di.loadImage

typealias myService = (Boolean, Services) -> Unit

class MyServiceAdapter(val myService: myService) :
    RecyclerView.Adapter<MyServiceAdapter.CategoryHolder>() {

    val serviceList = mutableListOf<Services>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        return CategoryHolder(
            LayoutUserItemBinding.inflate(
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

    inner class CategoryHolder(private val binding: LayoutUserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindUi(position: Int) {
            binding.apply {
                serviceList[position].let { _service ->
                    tvServiceTitle.text = _service.serviceName
                    tvCost.text = "Cost: ${_service.rate} sar"
                    if (position == 0)
                        tvDesc.text =
                            _service.description + _service.description + _service.description + _service.description + _service.description + _service.description + _service.description + _service.description + _service.description + _service.description
                    else
                        tvDesc.text = _service.description

                    ivServicePic.loadImage(_service.imageUrl + "")
                    ivServicePic.clipToOutline = true

                    //  Added bcs, when working with large data, recyclerview will reuse its old view,
                    //  thus making add button's view to display old data
                    updateButton(serviceList[position].isAdded, btnAdd)

                    btnAdd.setOnClickListener {
                        serviceList[position].isAdded = !serviceList[position].isAdded
                        updateButton(serviceList[position].isAdded, btnAdd)
                        //  myService.invoke(serviceList[position].isAdded, _service)
//                        notifyItemChanged(position)
                    }
                }
            }
        }

        private fun updateButton(isAdded: Boolean, btnAdd: Button) {
            if (isAdded) {
                btnAdd.text = itemView.context.getString(R.string.action_added)
                btnAdd.setTextColor(btnAdd.getCompatColor(R.color.white))
                btnAdd.setBackgroundColor(btnAdd.getCompatColor(R.color.yellow))
            } else {
                btnAdd.text = itemView.context.getString(R.string.action_add)
                btnAdd.setTextColor(btnAdd.getCompatColor(R.color.black))
                btnAdd.setBackgroundColor(btnAdd.getCompatColor(R.color.whitish_grey))
            }
        }

    }

}
