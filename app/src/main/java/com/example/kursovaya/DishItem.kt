package com.example.kursovaya

class DishItem(var name: String, var image: String, var composition: String="", var recipe: String="") {
    constructor() : this("", "", "", "")
}