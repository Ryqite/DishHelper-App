package com.example.kursovaya

import java.io.Serializable


class DishItem(var name: String, var image: String, var composition: String="", var recipe: String=""): Serializable {
    constructor() : this("", "", "", "")
}