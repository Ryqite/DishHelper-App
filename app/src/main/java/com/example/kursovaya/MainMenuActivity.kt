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

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_page)
        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)

        val dishes = listOf(
            Dish(R.drawable.dish1, "Spaghetti Bolognese"),
            Dish(R.drawable.dish2, "Caesar Salad"),
            Dish(R.drawable.dish3, "Pizza Margherita")
            // Добавьте другие блюда
        )

        for (dish in dishes) {
            val tableRow = TableRow(this)
            tableRow.layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
            tableRow.setPadding(0, 16, 0, 16)

            // ImageView для фото блюда
            val imageView = ImageView(this)
            imageView.setImageResource(dish.imageResId)
            imageView.layoutParams = TableRow.LayoutParams(150, 150) // Размеры изображения
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            tableRow.addView(imageView)

            // TextView для названия блюда
            val textView = TextView(this)
            textView.text = dish.name
            textView.textSize = 18f
            textView.setPadding(16, 0, 0, 0)
            textView.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            tableRow.addView(textView)

            // Добавление строки в TableLayout
            tableLayout.addView(tableRow)
        }
    }
    fun finishProcess(v: View){
        finishAffinity()
    }
}