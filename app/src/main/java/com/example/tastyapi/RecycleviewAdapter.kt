package com.example.tastyapi

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

@Suppress("DEPRECATION")
class RecycleviewAdapter(mainActivity: MainActivity, array: List<info>) :
    RecyclerView.Adapter<ViewHolder>() {
    private var array: List<info>
    private lateinit var viewHolder: ViewHolder
    private val mainActivity: MainActivity

    init {
        this.array = array
        this.mainActivity = mainActivity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.itemlayout, parent, false)
        viewHolder = ViewHolder(itemView)
        return viewHolder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.Builder(mainActivity.applicationContext).listener { picasso, url, exception ->
            Toast.makeText(mainActivity.applicationContext, exception.message.toString(), Toast.LENGTH_LONG).show()
        }
            .build().load(array[position].thumbnail_url)
            .into(holder.thumbnailImage)
        holder.name?.text =
            Html.fromHtml("<font>Name : </font><font color=${Color.RED}>${array[position].name}</font>")
        holder.createdAt?.text = "CreatedAt :" + array[position].createdAt

    }

    override fun getItemCount(): Int {
        return array.size
    }
}