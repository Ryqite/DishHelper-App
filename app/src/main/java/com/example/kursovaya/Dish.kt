package com.example.kursovaya

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class Dish(private val dishes: MutableList<DishItem>) : RecyclerView.Adapter<Dish.myHolder>() {
    override fun onBindViewHolder(
        holder: myHolder,
        position: Int
    ) {
        val model = dishes[position]
        holder.name.text = model.name
        Glide.with(holder.image.context)
            .load(model.image)
            .placeholder(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark)
            .error(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark_normal)
            .into(holder.image)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): myHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.dish_item, parent, false)
        return myHolder(view)
    }

    override fun getItemCount(): Int = dishes.size

    class myHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.dish_icon)
        var name: TextView = itemView.findViewById(R.id.dish_name)
    }
}