package com.example.kursovaya

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter

class ListDishes: FirebaseRecyclerAdapter{
    class myHolder : RecyclerView.ViewHolder{
        lateinit var img: ImageView
        lateinit var name: TextView

    }
}