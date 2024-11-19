package com.example.kursovaya
import com.bumptech.glide.Glide
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kursovaya.databinding.ActivityMainPageBinding
import com.example.kursovaya.databinding.DishItemBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
class ChoosedDish{
    companion object{
        var dishId:String=""
        var position:Int=0
    }
}
class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainPageBinding
    private lateinit var dishAdapter: DishPage
    var idList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!IsAdmin.isAdmin) {
            binding.searchName.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            val params = binding.searchName.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(15, 0, 15, 0) // установка отступов
            binding.searchName.layoutParams = params
            binding.floatingActionButton.visibility = View.GONE
        }

        // Установка LinearLayoutManager
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Загружаем блюда из Firebase
        loadDishes()

        // Поиск по составу
        binding.searchComposition.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                textCompositionSearch(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                textCompositionSearch(query)
                return true
            }
        })

        // Поиск по названию
        binding.searchName.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                textNameSearch(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                textNameSearch(query)
                return true
            }
        })
    }

    private fun loadDishes() {
        val dishesRef = FirebaseDatabase.getInstance().getReference("dishes")
        dishesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dishes = mutableListOf<DishItem>()
                idList.clear() // Очистим список перед загрузкой
                for (dishSnapshot in dataSnapshot.children) {
                    val dish = dishSnapshot.getValue(DishItem::class.java)
                    if (dish != null) {
                        dishes.add(dish)
                        idList.add(dishSnapshot.key ?: "") // Сохраняем ключ узла
                    }
                }
                // Устанавливаем адаптер с полученными данными и ключами
                dishAdapter = DishPage(dishes, idList)
                binding.recyclerView.adapter = dishAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("MainMenuActivity", "Ошибка загрузки данных: ${databaseError.message}")
            }
        })
    }
    fun finishProcess(v: View) {
        finishAffinity()
    }
    fun backToAuthentication(v: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
//    fun startDishPage(v: View) {
//        val dishId = ChoosedDish.dishId
//        val intent = Intent(this, ActivityDishPage::class.java)
//        intent.putExtra("dishID", dishId)
//        startActivity(intent)
//    }

    private fun textCompositionSearch(str: String) {
        val dishesRef = FirebaseDatabase.getInstance().getReference("dishes")
        dishesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dishes = mutableListOf<DishItem>()
                for (dishSnapshot in dataSnapshot.children) {
                    val dish = dishSnapshot.getValue(DishItem::class.java)
                    if (dish != null) {
                        dishes.add(dish)
                    }
                }

                val keyWords = str.split(",", ", ", " ,")
                val filteredDishes = dishes.filter { dish ->
                    keyWords.all { keyWord ->
                        dish.composition.contains(keyWord, ignoreCase = true)
                    }
                }

                // Устанавливаем фильтрованный список блюд
                dishAdapter = DishPage(filteredDishes,idList)
                binding.recyclerView.adapter = dishAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Firebase", "Ошибка: ${databaseError.message}")
            }
        })
    }

    private fun textNameSearch(str: String) {
        val dishesRef = FirebaseDatabase.getInstance().getReference("dishes")
        dishesRef.orderByChild("name")
            .startAt(str)
            .endAt(str + "\uf8ff")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val dishes = mutableListOf<DishItem>()
                    for (dishSnapshot in dataSnapshot.children) {
                        val dish = dishSnapshot.getValue(DishItem::class.java)
                        if (dish != null) {
                            dishes.add(dish)
                        }
                    }

                    // Устанавливаем фильтрованный список блюд
                    dishAdapter = DishPage(dishes,idList)
                    binding.recyclerView.adapter = dishAdapter
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("Firebase", "Ошибка: ${databaseError.message}")
                }
            })
    }

    override fun onStop() {
        super.onStop()
        // Остановка работы с Firebase
        if (::dishAdapter.isInitialized) {
            // Дополнительные действия при необходимости
        }
    }
}
