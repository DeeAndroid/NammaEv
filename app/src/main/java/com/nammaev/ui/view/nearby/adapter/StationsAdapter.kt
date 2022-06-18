package com.nammaev.ui.view.nearby.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nammaev.R
import com.nammaev.ui.view.nearby.data.MarkerData
import kotlin.collections.ArrayList

class StationsAdapter() : RecyclerView.Adapter<StationsAdapter.ViewHolder>() {
    var modelArrayList: ArrayList<MarkerData> = ArrayList<MarkerData>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StationsAdapter.ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_nearby_user, parent, false)
        return ViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: StationsAdapter.ViewHolder, position: Int) {
        holder.bindUI(position, modelArrayList)
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


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var selected = false
        fun bindUI(position: Int, list: ArrayList<MarkerData>) {

            itemView.tv_title.text=list[position].snippets
            Glide.with(itemView.user_cover.context)
                .load(list[position].avatar)
                .circleCrop()
                .into(itemView.user_cover)
        }

    }

}