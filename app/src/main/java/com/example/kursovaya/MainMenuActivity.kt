package com.example.kursovaya
import com.bumptech.glide.Glide
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kursovaya.databinding.ActivityMainPageBinding
import com.example.kursovaya.databinding.DishItemBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainPageBinding
    lateinit var listDishes: ListDishes
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.setLayoutManager(LinearLayoutManager(this))
        val options :FirebaseRecyclerOptions<DishItem> = FirebaseRecyclerOptions
            .Builder<DishItem>()
            .setQuery(FirebaseDatabase.getInstance().getReference().child("dishes"), DishItem::class.java)
            .build();
        listDishes= ListDishes(options)
        listDishes.startListening()
        binding.recyclerView.adapter=listDishes
    }

    fun finishProcess(v: View){
        finishAffinity()
    }
    override fun onStop() {
        super.onStop()
        listDishes.stopListening()
    }
}