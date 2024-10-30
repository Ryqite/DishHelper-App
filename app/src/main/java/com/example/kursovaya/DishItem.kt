package com.example.kursovaya

class DishItem(){
        constructor(
            _image: String,
            _name: String,
            _composition: String,
            _recipe: String
        ):this(){}
        var image: String
            set(_image) {
                this.image=_image
            }
            get() {
                return image
            }
        var name: String
            set(_name) {
                this.name=_name
            }
            get() {
                return name
            }
        var composition: String
            set(_composition) {
                this.composition=_composition
            }
            get() {
                return composition
            }
        var recipe: String
            set(_recipe) {
                this.recipe=_recipe
            }
            get() {
                return recipe
            }
}