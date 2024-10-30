package com.example.kursovaya

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

class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.setLayoutManager(LinearLayoutManager(this))
    }
    fun finishProcess(v: View){
        finishAffinity()
    }
}