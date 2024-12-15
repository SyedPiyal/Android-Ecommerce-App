package com.example.androidecommerceapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ProductEntity::class, CartEntity::class,OrderEntity::class,User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun userDao(): UserDao
}
