package com.example.blesdktest.adapter

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.blesdktest.databinding.ItemMainBinding
import com.inuker.bluetooth.library.search.SearchResult
import java.util.ArrayList

class BleScanViewAdapter(private var itemData: ArrayList<SearchResult>) :
    RecyclerView.Adapter<BleScanViewAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tv.text = itemData[position].address
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(itemData[position])
        }
    }


    override fun getItemCount(): Int = itemData?.size ?: 0

    interface OnItemClickCallback {
        fun onItemClicked(searchResult: SearchResult)
    }
}
