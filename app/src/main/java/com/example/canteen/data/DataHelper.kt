package com.example.canteen.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataHelper(context: Context) :
    SQLiteOpenHelper(context, "canteen.db", null, 7) {

    override fun onCreate(db: SQLiteDatabase) {

        // =========================
        // 👤 USERS
        // =========================
        db.execSQL(
            "CREATE TABLE users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "email TEXT UNIQUE," +
                    "password TEXT," +
                    "name TEXT," +
                    "role TEXT" +
                    ")"
        )

        // =========================
        // 🏪 CANTEENS
        // =========================
        db.execSQL(
            "CREATE TABLE canteens (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "ownerId INTEGER" +
                    ")"
        )

        // =========================
        // 📝 SELLER REQUESTS
        // =========================
        db.execSQL(
            "CREATE TABLE seller_requests (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "userId INTEGER," +
                    "canteenName TEXT," +
                    "description TEXT," +
                    "status TEXT" +
                    ")"
        )

        // =========================
        // 🍔 MENU
        // =========================
        db.execSQL(
            "CREATE TABLE menu (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "canteenId INTEGER," +
                    "name TEXT," +
                    "price TEXT," +
                    "imageUri TEXT" +
                    ")"
        )

        // 🔥 ADMIN DEFAULT
        db.execSQL(
            "INSERT INTO users (email, password, name, role) VALUES (" +
                    "'admin@gmail.com'," +
                    "'admin123'," +
                    "'Admin'," +
                    "'admin'" +
                    ")"
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {

        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS canteens")
        db.execSQL("DROP TABLE IF EXISTS seller_requests")
        db.execSQL("DROP TABLE IF EXISTS menu")

        onCreate(db)
    }

    // =========================
    // 🔐 USER
    // =========================

    fun register(
        email: String,
        password: String,
        name: String,
        role: String
    ): Boolean {

        val db = writableDatabase

        val check = db.rawQuery(
            "SELECT * FROM users WHERE email=?",
            arrayOf(email)
        )

        if (check.count > 0) {
            check.close()
            return false
        }

        check.close()

        val values = ContentValues().apply {
            put("email", email)
            put("password", password)
            put("name", name)
            put("role", role)
        }

        db.insert("users", null, values)

        return true
    }

    fun login(
        email: String,
        password: String
    ): String? {

        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT role FROM users WHERE email=? AND password=?",
            arrayOf(email, password)
        )

        var role: String? = null

        if (cursor.moveToFirst()) {

            role = cursor.getString(
                cursor.getColumnIndexOrThrow("role")
            )
        }

        cursor.close()

        return role
    }

    fun getUserName(email: String): String {

        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT name FROM users WHERE email=?",
            arrayOf(email)
        )

        var name = "Guest"

        if (cursor.moveToFirst()) {

            name = cursor.getString(
                cursor.getColumnIndexOrThrow("name")
            )
        }

        cursor.close()

        return name
    }

    // =========================
    // 🏪 CANTEENS
    // =========================

    fun insertCanteen(
        name: String,
        ownerId: Int
    ) {

        val db = writableDatabase

        val values = ContentValues().apply {
            put("name", name)
            put("ownerId", ownerId)
        }

        db.insert(
            "canteens",
            null,
            values
        )
    }

    fun getCanteenNameById(
        id: Int
    ): String {

        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT name FROM canteens WHERE id=?",
            arrayOf(id.toString())
        )

        var name = ""

        if (cursor.moveToFirst()) {

            name = cursor.getString(
                cursor.getColumnIndexOrThrow("name")
            )
        }

        cursor.close()

        return name
    }

    // =========================
    // 🍔 MENU
    // =========================

    fun insertMenu(
        canteenId: Int,
        name: String,
        price: String,
        imageUri: String
    ) {

        val db = writableDatabase

        val values = ContentValues().apply {
            put("canteenId", canteenId)
            put("name", name)
            put("price", price)
            put("imageUri", imageUri)
        }

        db.insert(
            "menu",
            null,
            values
        )
    }

    fun getAllMenu(): MutableList<MenuItem> {

        val list = mutableListOf<MenuItem>()

        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM menu",
            null
        )

        if (cursor.moveToFirst()) {

            do {

                val canteenId = cursor.getInt(
                    cursor.getColumnIndexOrThrow("canteenId")
                )

                val item = MenuItem(

                    id = cursor.getInt(
                        cursor.getColumnIndexOrThrow("id")
                    ),

                    canteenId = canteenId,

                    canteenName = getCanteenNameById(
                        canteenId
                    ),

                    name = cursor.getString(
                        cursor.getColumnIndexOrThrow("name")
                    ),

                    price = cursor.getString(
                        cursor.getColumnIndexOrThrow("price")
                    ),

                    imageUri = cursor.getString(
                        cursor.getColumnIndexOrThrow("imageUri")
                    ) ?: ""
                )

                list.add(item)

            } while (cursor.moveToNext())
        }

        cursor.close()

        return list
    }

    fun getMenuById(id: Int): MenuItem? {

        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM menu WHERE id=?",
            arrayOf(id.toString())
        )

        var menu: MenuItem? = null

        if (cursor.moveToFirst()) {

            val canteenId = cursor.getInt(
                cursor.getColumnIndexOrThrow("canteenId")
            )

            menu = MenuItem(

                id = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id")
                ),

                canteenId = canteenId,

                canteenName = getCanteenNameById(
                    canteenId
                ),

                name = cursor.getString(
                    cursor.getColumnIndexOrThrow("name")
                ),

                price = cursor.getString(
                    cursor.getColumnIndexOrThrow("price")
                ),

                imageUri = cursor.getString(
                    cursor.getColumnIndexOrThrow("imageUri")
                ) ?: ""
            )
        }

        cursor.close()

        return menu
    }

    fun updateMenuById(
        id: Int,
        name: String,
        price: String,
        imageUri: String,
        canteenId: Int
    ) {

        val db = writableDatabase

        val values = ContentValues().apply {

            put("canteenId", canteenId)
            put("name", name)
            put("price", price)
            put("imageUri", imageUri)
        }

        db.update(
            "menu",
            values,
            "id=?",
            arrayOf(id.toString())
        )
    }

    fun deleteMenu(id: Int) {

        val db = writableDatabase

        db.delete(
            "menu",
            "id=?",
            arrayOf(id.toString())
        )
    }
}