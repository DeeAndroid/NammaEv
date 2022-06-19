package com.nammaev.ui.view.nearby

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nammaev.databinding.ItemNearbyUserBinding
import com.nammaev.di.loadImage
import com.nammaev.ui.view.nearby.data.MarkerData
import com.nammaev.ui.view.nearby.interfaces.OnStationClicked

class StationsAdapter(var onStationClicked: OnStationClicked) : RecyclerView.Adapter<StationsAdapter.ViewHolder>() {
    var modelArrayList = mutableListOf<MarkerData>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(
        ItemNearbyUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindUI(position, modelArrayList,onStationClicked)
    }

    override fun getItemCount(): Int {
        return modelArrayList.size
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun setModelArrayList(_modelArrayList: List<MarkerData>?) {
        modelArrayList.clear()
        modelArrayList.addAll(_modelArrayList!!)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemNearbyUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindUI(position: Int, list: ArrayList<MarkerData>, onStationClicked: OnStationClicked) {

            binding.apply {
                userCover.loadImage(list[position].avatar)
                binding.parent.setOnClickListener {
                    onStationClicked.onStationClicked(list[position],position)
                }

            }
        }
    }

}