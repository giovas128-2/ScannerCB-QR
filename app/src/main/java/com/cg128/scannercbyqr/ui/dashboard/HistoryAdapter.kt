package com.cg128.scannercbyqr.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cg128.scannercbyqr.R
import com.cg128.scannercbyqr.data.ScanHistory

class HistoryAdapter(private var items: List<ScanHistory>,
                     private val onItemClick: (ScanHistory) -> Unit) :  // 👈 callback
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvNameProduct)
        val tvDate: TextView = view.findViewById(R.id.tvFechaProduct)
        val img: ImageView = view.findViewById(R.id.imgProduct)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_scanner, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = items[position]
        holder.tvName.text = item.text
        holder.tvDate.text = item.date

        val imageUrl = item.imageUrl
        if (imageUrl != null) {
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .into(holder.img)
        } else {
            holder.img.setImageResource(R.drawable.ic_no_image)
        }
        // 👇 aquí va el clic
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }

    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<ScanHistory>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun clearAll() {
        items = emptyList()
        notifyDataSetChanged()
    }
}
