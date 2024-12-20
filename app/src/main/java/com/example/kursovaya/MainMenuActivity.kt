package com.example.kursovaya
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kursovaya.databinding.ActivityMainPageBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainPageBinding
    private lateinit var dishAdapter: DishPage
    var idList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.floatingActionButton.setOnClickListener {

            val bottomSheetDialog = BottomSheetDialog(this)
            val dialogView = layoutInflater.inflate(R.layout.choose_option, null)
            bottomSheetDialog.setContentView(dialogView)
            bottomSheetDialog.show()

            val btnCreate: Button = dialogView.findViewById(R.id.createDish)
            val btnUpdate: Button = dialogView.findViewById(R.id.updateDish)
            val btnDelete: Button = dialogView.findViewById(R.id.deleteDish)

            btnCreate.setOnClickListener {
                bottomSheetDialog.dismiss()
                val createDialog = BottomSheetDialog(this)
                val createView = layoutInflater.inflate(R.layout.create_option, null)
                createDialog.setContentView(createView)
                createDialog.show()
                val createDishID: EditText = createView.findViewById(R.id.CreateDishID)
                val createName: EditText = createView.findViewById(R.id.CreateName)
                val createImage: EditText = createView.findViewById(R.id.CreateImage)
                val createComposition: EditText = createView.findViewById(R.id.CreateComposition)
                val createRecipe: EditText = createView.findViewById(R.id.CreateRecipe)

                // Настройка кнопок и других элементов в create_option
                val confirmButton: Button = createView.findViewById(R.id.createButton)
                confirmButton.setOnClickListener {
                    val dishId = createDishID.text.toString().trim()
                    val name = createName.text.toString().trim()
                    val image = createImage.text.toString().trim()
                    val composition = createComposition.text.toString().trim()
                    val recipe = createRecipe.text.toString().trim()

                    if (dishId.isEmpty() || name.isEmpty() || composition.isEmpty() || recipe.isEmpty()) {
                        Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                    } else {
                        saveDishToFirebase(dishId, name, image, composition, recipe)
                        createDialog.dismiss()
                    }
                }
            }

            btnUpdate.setOnClickListener {
                bottomSheetDialog.dismiss()
                val updateDialog = BottomSheetDialog(this)
                val updateView = layoutInflater.inflate(R.layout.update_option, null)
                updateDialog.setContentView(updateView)
                updateDialog.show()

                // Настройка кнопок и других элементов в update_option
                val updateButton: Button = updateView.findViewById(R.id.updateButton)
                val updateDishId: EditText = updateView.findViewById(R.id.updateDishId)
                val updateName: EditText = updateView.findViewById(R.id.updateName)
                val updateImage: EditText = updateView.findViewById(R.id.updateImage)
                val updateComposition: EditText = updateView.findViewById(R.id.updateComposition)
                val updateRecipe: EditText = updateView.findViewById(R.id.updateRecipe)

                updateButton.setOnClickListener {
                    val dishId = updateDishId.text.toString().trim()
                    val name = updateName.text.toString().trim()
                    val image = updateImage.text.toString().trim()
                    val composition = updateComposition.text.toString().trim()
                    val recipe = updateRecipe.text.toString().trim()

                    if (dishId.isEmpty()) {
                        Toast.makeText(this, "DishID is required", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    // Обновление блюда
                    updateDishInFirebase(dishId, name, image, composition, recipe)
                    updateDialog.dismiss()
                }
            }
            btnDelete.setOnClickListener {
                bottomSheetDialog.dismiss()
                val deleteDialog = BottomSheetDialog(this)
                val deleteView = layoutInflater.inflate(R.layout.delete_option, null)
                deleteDialog.setContentView(deleteView)
                deleteDialog.show()

                // Настройка кнопок и других элементов в delete_option
                val deleteDishId: EditText=deleteView.findViewById(R.id.deleteDishId)
                val confirmButton: Button = deleteView.findViewById(R.id.deleteButton)
                confirmButton.setOnClickListener {
                    val dishId: String=deleteDishId.text.toString().trim()
                    if (dishId.isNotEmpty()) {
                        deleteDishFromFirebase(dishId)
                        deleteDialog.dismiss()
                    } else {
                        Toast.makeText(this, "Please enter a valid Dish ID", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
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
    private fun saveDishToFirebase(dishId: String, name: String, image: String, composition: String, recipe: String) {
        val database = FirebaseDatabase.getInstance().getReference("dishes")
        val dish = DishItem(name, image, composition, recipe)

        // Сохраняем данные в Firebase
        database.child(dishId).setValue(dish)
            .addOnSuccessListener {
                Toast.makeText(this, "Dish successfully created!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to create dish: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun updateDishInFirebase(dishId: String, name: String, image: String, composition: String, recipe: String) {
        val dishesRef = FirebaseDatabase.getInstance().getReference("dishes")

        dishesRef.child(dishId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val updatedData = mutableMapOf<String, Any>()

                    if (name.isNotEmpty()) updatedData["name"] = name
                    if (image.isNotEmpty()) updatedData["image"] = image
                    if (composition.isNotEmpty()) updatedData["composition"] = composition
                    if (recipe.isNotEmpty()) updatedData["recipe"] = recipe

                    // Обновление данных блюда
                    dishesRef.child(dishId).updateChildren(updatedData)
                        .addOnSuccessListener {
                            Toast.makeText(this@MainMenuActivity, "Dish updated successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { error ->
                            Toast.makeText(this@MainMenuActivity, "Error updating dish: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this@MainMenuActivity, "Dish not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Firebase", "Error: ${databaseError.message}")
            }
        })
    }
    private fun deleteDishFromFirebase(dishId: String) {
        val dishesRef = FirebaseDatabase.getInstance().getReference("dishes")

        dishesRef.child(dishId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    dishesRef.child(dishId).removeValue()
                        .addOnSuccessListener {
                            Toast.makeText(this@MainMenuActivity, "Dish deleted successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { error ->
                            Toast.makeText(this@MainMenuActivity, "Error deleting dish: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this@MainMenuActivity, "Dish not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Firebase", "Error: ${databaseError.message}")
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
