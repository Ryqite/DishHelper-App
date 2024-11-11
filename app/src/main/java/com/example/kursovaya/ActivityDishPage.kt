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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityDishPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dishId: String = intent.getStringExtra("dishId").toString();
        Log.d("ActivityDishPage", "Received dishId: $dishId")
        val dishRef:DatabaseReference = FirebaseDatabase.getInstance().getReference("dishes").child(dishId)
        dishRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val name = snapshot.child("name").getValue(String::class.java)
                    val composition = snapshot.child("composition").getValue(String::class.java)
                    val recipe = snapshot.child("recipe").getValue(String::class.java)
                    val imageUrl = snapshot.child("imageUrl").getValue(String::class.java)

                    // Используйте binding для доступа к элементам интерфейса
                    binding.dishPageText.text = name
                    binding.compositionText.text = composition
                    binding.recipeText.text = recipe

                    Glide.with(this@ActivityDishPage)
                        .load(imageUrl)
                        .into(binding.imageViewDishPage)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ActivityDishPage", "Error loading data: ${error.message}")
            }
        })
    }
}
