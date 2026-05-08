package com.example.canteen.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun BottomNavBar(
    navController: NavController,
    currentRoute: String
) {

    BottomAppBar {

        NavigationBarItem(

            selected = currentRoute == "home",

            onClick = {
                navController.navigate("home")
            },

            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home"
                )
            },

            label = {
                Text("Home")
            }
        )

        NavigationBarItem(

            selected = currentRoute == "seller_request",

            onClick = {
                navController.navigate("seller_request")
            },

            icon = {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Seller"
                )
            },

            label = {
                Text("Seller")
            }
        )

        NavigationBarItem(

            selected = currentRoute == "profile",

            onClick = {
                navController.navigate("profile")
            },

            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile"
                )
            },

            label = {
                Text("Profile")
            }
        )
    }
}