package com.qdot.disasteralerter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.qdot.disasteralerter.databinding.UpdateLayoutBinding
import com.qdot.disasteralerter.model.UpdateData

class UpdateAdapter(private var dataList : MutableList<UpdateData>) :
    RecyclerView.Adapter<UpdateAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: UpdateLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UpdateLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(dataList[position]){
                binding.titleTv.text = this.type +" at "+this.time
                binding.dateTv.text = this.pubTime
                binding.msgTv.text = this.msg
            }
        }
    }
}