package com.example.tastyapi

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
    var thumbnailImage: ImageView? = null
    var name: TextView? = null
    var createdAt: TextView? = null

    init {
        thumbnailImage = itemView.findViewById(R.id.thumbnailImage)
        name = itemView.findViewById(R.id.name)
        createdAt = itemView.findViewById(R.id.createdAt)
    }
}