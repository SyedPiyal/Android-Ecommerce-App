package com.example.androidecommerceapp.repository

import com.example.androidecommerceapp.database.User
import com.example.androidecommerceapp.database.UserDao
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow


// for using roomdb

class UserRepository @Inject constructor(private val userDao: UserDao) {

    suspend fun registerUser(user: User) {
        userDao.insert(user)
    }

    fun loginUser(email: String, password: String): Flow<User?> {
        return userDao.getUserByEmailAndPassword(email, password)
    }

    fun checkIfUserExists(email: String): Flow<User?> {
        return userDao.getUserByEmail(email)
    }

    suspend fun updateUser(user: User) {
        userDao.insert(user)
    }

}

