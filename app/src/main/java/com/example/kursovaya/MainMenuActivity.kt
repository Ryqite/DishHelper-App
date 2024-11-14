package com.example.kursovaya
import com.bumptech.glide.Glide
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.SearchView
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainPageBinding
    lateinit var listDishes: ListDishes
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.setLayoutManager(LinearLayoutManager(this))
        val options: FirebaseRecyclerOptions<DishItem> = FirebaseRecyclerOptions
            .Builder<DishItem>()
            .setQuery(
                FirebaseDatabase.getInstance().getReference().child("dishes"),
                DishItem::class.java
            )
            .build();
        listDishes = ListDishes(options)
        listDishes.startListening()
        binding.recyclerView.adapter = listDishes
        binding.searchComposition.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Вызывается при отправке поискового запроса
                textCompositionSearch(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                // Вызывается при изменении текста поискового запроса
                textCompositionSearch(query)
                return true
            }

        })
        binding.searchName.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Вызывается при отправке поискового запроса
                textNameSearch(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                // Вызывается при изменении текста поискового запроса
                textNameSearch(query)
                return true
            }

        })
    }

    fun finishProcess(v: View) {
        finishAffinity()
    }

    fun startDishPage(v: View) {
        val intent = Intent(this, ActivityDishPage::class.java)
        intent.putExtra("dishId", "dish1")
        startActivity(intent)
    }
    fun backToAuthentication(v: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    override fun onStop() {
        super.onStop()
        listDishes.stopListening()
    }
    fun textCompositionSearch(str: String) {
        val dishesRef = FirebaseDatabase.getInstance().getReference("dishes")
        dishesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dishes = mutableListOf<DishItem>()
                for (dishSnapshot in dataSnapshot.children) {
                    val dish = dishSnapshot.getValue(DishItem::class.java)
                    if (dish != null) {
                        dishes.add(dish)
                    }
                }
                var keyWords=str.split(",",", "," ,")
                val filteredDishes = dishes.filter {dish->
                    keyWords.all { keyWord->
                         dish.composition.contains(keyWord,ignoreCase = true)
                    }
                }.toMutableList()
                val adapter = Dish(filteredDishes)
                binding.recyclerView.adapter = adapter
                }
            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибок
                Log.e("Firebase", "Ошибка: ${databaseError.message}")
            }
        })
    }
    fun textNameSearch(str: String) {
        val options :FirebaseRecyclerOptions<DishItem> = FirebaseRecyclerOptions
            .Builder<DishItem>()
            .setQuery(FirebaseDatabase.getInstance().getReference().child("dishes").orderByChild("name").startAt(str).endAt(str + "\uf8ff"), DishItem::class.java)
            .build();
        val sortedList: ListDishes= ListDishes(options)
        sortedList.startListening()
        binding.recyclerView.adapter=sortedList
    }
}
