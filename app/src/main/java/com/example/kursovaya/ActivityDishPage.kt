package com.example.kursovaya

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.kursovaya.databinding.ActivityDishPageBinding
import com.example.kursovaya.databinding.DishItemBinding

class ActivityDishPage : AppCompatActivity() {
    private lateinit var binding: ActivityDishPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dish_page)
        binding= ActivityDishPageBinding.inflate(layoutInflater)
    }
    fun startDishPage(v: View){
        val intent = Intent(this, ActivityDishPage::class.java)
        startActivity(intent)
    }
}
