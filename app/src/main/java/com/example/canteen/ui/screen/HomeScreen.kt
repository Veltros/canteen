package com.example.canteen.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import com.example.canteen.data.DataHelper
import com.example.canteen.data.MenuItem

import com.example.canteen.ui.component.MenuCard
import com.example.canteen.ui.component.TopBar
import com.example.canteen.ui.component.BottomNavBar

import com.example.canteen.ui.theme.GrayBg

@Composable
fun HomeScreen(navController: NavController) {

    val context = LocalContext.current

    val db = remember {
        DataHelper(context)
    }

    val menuList = remember {
        mutableStateListOf<MenuItem>()
    }

    // 🔥 LOAD MENU
    LaunchedEffect(Unit) {

        menuList.clear()

        menuList.addAll(
            db.getAllMenu()
        )
    }

    Scaffold(

        bottomBar = {

            BottomNavBar(
                navController = navController,
                currentRoute = "home"
            )
        }

    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(GrayBg)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(14.dp)
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                TopBar(navController)

                Spacer(modifier = Modifier.height(20.dp))

                // 🔥 EMPTY
                if (menuList.isEmpty()) {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "Belum ada menu 😢",
                            color = Color.Gray
                        )
                    }

                } else {

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ){

                        itemsIndexed(menuList) { index, item ->

                            MenuCard(
                                item = item,

                                onEdit = {
                                    navController.navigate(
                                        "edit_menu/${item.id}"
                                    )
                                },

                                onDelete = {

                                    db.deleteMenu(item.id)

                                    menuList.removeAt(index)
                                }
                            )
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = {
                    navController.navigate("add_menu")
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        bottom = 80.dp,
                        end = 16.dp
                    )
            ) {

                Text("+")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {

    HomeScreen(
        navController = rememberNavController()
    )
}