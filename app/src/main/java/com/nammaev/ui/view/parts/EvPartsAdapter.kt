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
import com.nammaev.data.network.api.response.Product
import com.nammaev.databinding.LayoutEvPartsItemBinding
import com.nammaev.di.loadImage

typealias myService = (Product) -> Unit

class EvPartsAdapter(val myService: myService) :
    RecyclerView.Adapter<EvPartsAdapter.CategoryHolder>() {

    val productList = mutableListOf<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        return CategoryHolder(
            LayoutEvPartsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = productList.size

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bindUi(position)
    }

    fun addProducts(_categoryList: List<Product>) {
        productList.clear()
        productList.addAll(_categoryList)
        notifyDataSetChanged()
    }

    inner class CategoryHolder(private val binding: LayoutEvPartsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindUi(position: Int) {
            binding.apply {
                productList[position].let { product ->
                    tvPartTitle.text = product.name
                    tvCost.text = "Cost: ${product.price} sar"
                    tvDesc.text = product.description

                    ivProductPic.loadImage(product.icon.toString())

                    ivProductPic.clipToOutline = true

                    btnBuy.setOnClickListener {
                        myService.invoke(productList[position])
                    }
                }
            }
        }

    }

}
