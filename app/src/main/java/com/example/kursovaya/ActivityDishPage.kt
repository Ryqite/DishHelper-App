package com.example.kursovaya

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.kursovaya.databinding.ActivityDishPageBinding
import com.example.kursovaya.databinding.DishItemBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ActivityDishPage : AppCompatActivity() {
    private lateinit var binding: ActivityDishPageBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Используем View Binding
        binding = ActivityDishPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получаем объект блюда из Intent
        val dish = intent.getSerializableExtra("dish") as? DishItem
        if (dish != null) {
            displayDishData(dish)
        }
    }

    private fun displayDishData(dish: DishItem) {
        binding.dishPageText.text = dish.name
        binding.recipeText.text = dish.recipe
        binding.compositionText.text = dish.composition

        Glide.with(this)
            .load(dish.image)
            .placeholder(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark)
            .error(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark_normal)
            .into(binding.imageViewDishPage)
    }
}