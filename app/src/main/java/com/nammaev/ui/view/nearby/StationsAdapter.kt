package com.nammaev.ui.view.nearby

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nammaev.databinding.ItemNearbyUserBinding
import com.nammaev.ui.view.nearby.data.MarkerData
import com.nammaev.ui.view.nearby.interfaces.OnStationClicked

class StationsAdapter(var onStationClicked: OnStationClicked) : RecyclerView.Adapter<StationsAdapter.ViewHolder>() {
    var modelArrayList: ArrayList<MarkerData> = ArrayList<MarkerData>()

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
        modelArrayList.addAll(_modelArrayList!!)
    }


    inner class ViewHolder(val binding: ItemNearbyUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var selected = false
        fun bindUI(position: Int, list: ArrayList<MarkerData>, onStationClicked: OnStationClicked) {

            binding.apply {
                tvTitle.text = list[position].snippets
                Glide.with(userCover.context)
                    .load(list[position].avatar)
                    .circleCrop()
                    .into(userCover)

                binding.parent.setOnClickListener {
                    onStationClicked.onStationClicked(list[position].avatar)
                }

            }
        }
    }

}