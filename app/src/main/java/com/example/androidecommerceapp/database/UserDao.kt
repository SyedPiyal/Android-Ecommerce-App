package com.example.androidecommerceapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM user_table WHERE email = :email AND password = :password LIMIT 1")
    fun getUserByEmailAndPassword(email: String, password: String): Flow<User?>

    @Query("SELECT * FROM user_table WHERE email = :email LIMIT 1")
    fun getUserByEmail(email: String): Flow<User?>
}
