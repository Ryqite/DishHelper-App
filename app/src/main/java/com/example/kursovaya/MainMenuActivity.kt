package com.example.kursovaya
import com.bumptech.glide.Glide
import android.content.Intent
import android.os.Bundle
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
        val searchView: SearchView=binding.searchName
        if(searchView.query.isEmpty())
        {
            binding.recyclerView.adapter=listDishes
        }
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Вызывается при отправке поискового запроса
                textSearch(query)
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                // Вызывается при изменении текста поискового запроса
                textSearch(query)
                return true
            }
        })
    }

    fun finishProcess(v: View){
        finishAffinity()
    }
    fun startDishPage(v: View){
    val intent = Intent(this, ActivityDishPage::class.java)
    intent.putExtra("dishId", "dish1")
    startActivity(intent)
    }
    override fun onStop() {
        super.onStop()
        listDishes.stopListening()
    }
    fun textSearch(str: String){
        val options :FirebaseRecyclerOptions<DishItem> = FirebaseRecyclerOptions
            .Builder<DishItem>()
            .setQuery(FirebaseDatabase.getInstance().getReference().child("dishes").orderByChild("name").startAt(str).endAt(str+"."), DishItem::class.java)
            .build();
        val sortedList: ListDishes= ListDishes(options)
        sortedList.startListening()
        binding.recyclerView.adapter=sortedList
    }
}