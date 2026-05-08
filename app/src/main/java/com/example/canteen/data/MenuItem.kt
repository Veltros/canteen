package com.example.canteen.data

data class MenuItem(
    val id: Int = 0,
    val canteenId: Int,
    val canteenName: String,
    val name: String,
    val price: String,
    val imageUri: String
)