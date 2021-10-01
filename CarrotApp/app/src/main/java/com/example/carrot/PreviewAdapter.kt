package com.example.carrot

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.NumberFormat
import java.util.*

class PreviewAdapter(private val dataSet: MutableList<PreviewAdapter.PreviewData>) :
    RecyclerView.Adapter<PreviewAdapter.ViewHolder>() {

    class PreviewData(_picture: String, _title: String, _info: String, _price: Int, _id: Int) {
        val picture:String = _picture
        val title:String = _title
        val info:String = _info
        val price:Int = _price
        val id:Int = _id
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val picture: ImageView
        val title: TextView
        val info: TextView
        val price: TextView

        init {
            picture = view.findViewById(R.id.preview_pic)
            title = view.findViewById(R.id.preview_title)
            info = view.findViewById(R.id.preview_info)
            price = view.findViewById(R.id.preview_price)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.layout_main_preview, viewGroup, false)
        view.setOnClickListener{
            val detailActivity = Intent(viewGroup.context, DetailActivity::class.java)
            detailActivity.putExtra("id", view.tag as Int)
            viewGroup.context.startActivity(detailActivity)
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Glide.with(viewHolder.itemView).load(dataSet[position].picture).into(viewHolder.picture)
        viewHolder.title.text = dataSet[position].title
        viewHolder.info.text = dataSet[position].info
        viewHolder.price.text = NumberFormat.getInstance(Locale.getDefault()).format(dataSet[position].price) + "원"
        viewHolder.itemView.tag = dataSet[position].id
    }

    override fun getItemCount() = dataSet.size

}