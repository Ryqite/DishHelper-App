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
        setContentView(R.layout.activity_dish_page)

        val dishKey = intent.getStringExtra("dishKey")
        if (dishKey != null) {
            loadDishData(dishKey)
        }
    }

    private fun loadDishData(dishKey: String) {
        val database = FirebaseDatabase.getInstance().getReference("dishes").child(dishKey)

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dish = snapshot.getValue(DishItem::class.java)
                if (dish != null) {
                    binding.dishPageText.text = dish.name
                    binding.recipeText.text = dish.recipe
                    binding.compositionText.text = dish.composition
                    Glide.with(this@ActivityDishPage)
                        .load(dish.image)
                        .into(binding.imageViewDishPage)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error: ${error.message}")
            }
        })
    }
}