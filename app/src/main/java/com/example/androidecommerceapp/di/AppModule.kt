package com.example.androidecommerceapp.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.example.androidecommerceapp.database.AppDatabase
import com.example.androidecommerceapp.database.CartDao
import com.example.androidecommerceapp.database.FavoriteDao
import com.example.androidecommerceapp.database.OrderDao
import com.example.androidecommerceapp.database.ProductDao
import com.example.androidecommerceapp.database.UserDao
import com.example.androidecommerceapp.view.myCart.repository.CartRepository
import com.example.androidecommerceapp.view.orderHistory.repository.OrderRepository
import com.example.androidecommerceapp.view.productDetails.repository.ProductRepositoryDao
import com.example.androidecommerceapp.view.auth.repository.UserRepository
import com.example.androidecommerceapp.view.favorites.repository.FavoriteRepository
import com.example.androidecommerceapp.view.home.repository.ProductRepository
import com.example.androidecommerceapp.view.service.ApiService
import com.example.androidecommerceapp.view.service.FakeStoreApiService
import com.example.androidecommerceapp.view.myCart.viewModel.MyCartViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @Named("reqres")
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://reqres.in/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Second Retrofit client for fakestoreapi.com API
    @Provides
    @Singleton
    @Named("fakestoreapi")
    fun provideRetrofitFakeStore(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(@Named("reqres") retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }


    @Provides
    @Singleton
    fun provideApiServiceFakeStore(@Named("fakestoreapi") retrofit: Retrofit): FakeStoreApiService {
        return retrofit.create(FakeStoreApiService::class.java)
    }

    // room database
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideProductDao(database: AppDatabase): ProductDao {
        return database.productDao()
    }

    @Provides
    @Singleton
    fun provideProductRepository(productDao: ProductDao): ProductRepositoryDao {
        return ProductRepositoryDao(productDao)
    }

    @Provides
    @Singleton
    fun provideCartDao(database: AppDatabase): CartDao {
        return database.cartDao()
    }

    @Provides
    @Singleton
    fun provideCartRepository(cartDao: CartDao): CartRepository {
        return CartRepository(cartDao)
    }

    @Provides
    @Singleton
    fun provideCartViewModel(cartRepository: CartRepository): MyCartViewModel {
        return MyCartViewModel(cartRepository)
    }

    @Provides
    @Singleton
    fun provideOrderDao(database: AppDatabase): OrderDao {
        return database.orderDao()
    }

    @Provides
    @Singleton
    fun provideOrderRepository(orderDao: OrderDao): OrderRepository {
        return OrderRepository(orderDao)
    }


    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepository(userDao)
    }

    @Provides
    @Singleton
    fun provideFavoritesDao(database: AppDatabase): FavoriteDao {
        return database.favoritesDao()
    }

    @Provides
    @Singleton
    fun provideFavoritesRepository(favoritesDao: FavoriteDao): FavoriteRepository {
        return FavoriteRepository(favoritesDao)
    }

    @Provides
    @Singleton
    fun provideHomeRepository(apiService: FakeStoreApiService): ProductRepository {
        return ProductRepository(apiService)
    }


    // for work manager

    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

}