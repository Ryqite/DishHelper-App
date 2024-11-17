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
import com.example.kursovaya.databinding.DishPageBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ActivityDishPage : AppCompatActivity() {
    private lateinit var binding: ActivityDishPageBinding
    private lateinit var binding2: DishPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityDishPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dishId = intent.getStringExtra("dishId")
        val dishRef:DatabaseReference = FirebaseDatabase.getInstance().getReference("dishes").child(dishId.toString())
        val dishPages = mutableListOf<DishItem>()
        val adapter= DishPage(dishPages)
        binding.recyclerDishPage.adapter=adapter
        binding.recyclerDishPage.layoutManager = LinearLayoutManager(this)
        dishRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dish = dataSnapshot.getValue(DishItem::class.java)
                if (dish != null) {

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ActivityDishPage", "Error loading data: ${error.message}")
            }
        })
    }
}
