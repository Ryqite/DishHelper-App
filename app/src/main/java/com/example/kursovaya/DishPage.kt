package com.example.kursovaya

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kursovaya.databinding.DishItemBinding

class DishPage(val dishes: List<DishItem>, private val idList: List<String>) :
RecyclerView.Adapter<DishPage.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding = DishItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val dish = dishes[position]
        val dishID = idList[position] // Получаем ключ узла для текущей позиции
        holder.bind(dish)

        // Установка обработчика клика на элемент
        holder.itemView.setOnClickListener { view ->
            Log.d("DishPage", "Clicked on: ${dish.name}")
            val intent = Intent(view.context, ActivityDishPage::class.java)
            intent.putExtra("dishID", dishID) // Передаем ключ узла
            view.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = dishes.size

    class MyHolder(private val binding: DishItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dish: DishItem) {
            binding.dishName.text = dish.name
            Glide.with(binding.dishIcon.context)
                .load(dish.image)
                .placeholder(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark)
                .error(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(binding.dishIcon)
        }
    }
}
