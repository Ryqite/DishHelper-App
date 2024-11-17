package com.example.kursovaya
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
class DishPage(private val dishPages: MutableList<DishItem>) : RecyclerView.Adapter<DishPage.myHolder>() {
    override fun onBindViewHolder(
        holder: myHolder,
        position: Int
    ) {
        val model = dishPages[position]
        holder.name.text = model.name
        holder.recipe.text = model.recipe
        holder.composition.text = model.composition
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
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.dish_page, parent, false)
        return myHolder(view)
    }
    override fun getItemCount(): Int = dishPages.size

    class myHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.imageView_dish_page)
        var name: TextView = itemView.findViewById(R.id.dish_page_text)
        var composition: TextView = itemView.findViewById(R.id.composition_text)
        var recipe: TextView = itemView.findViewById(R.id.recipe_text)
    }
}