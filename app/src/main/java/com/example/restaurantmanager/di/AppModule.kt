package com.example.restaurantmanager.di

import android.content.Context
import androidx.room.Room
import com.example.restaurantmanager.data.local.AppDatabase
import com.example.restaurantmanager.data.local.dao.CategoryDao
import com.example.restaurantmanager.data.local.dao.MenuItemDao
import com.example.restaurantmanager.data.local.dao.OrderDao
import com.example.restaurantmanager.data.repository.RestaurantRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "restaurant_manager.db"
        ).build()
    }

    @Provides
    fun provideMenuItemDao(appDatabase: AppDatabase): MenuItemDao {
        return appDatabase.menuItemDao()
    }

    @Provides
    fun provideCategoryDao(appDatabase: AppDatabase): CategoryDao {
        return appDatabase.categoryDao()
    }

    @Provides
    fun provideOrderDao(appDatabase: AppDatabase): OrderDao {
        return appDatabase.orderDao()
    }

    @Provides
    @Singleton
    fun provideRestaurantRepository(
        menuItemDao: MenuItemDao,
        categoryDao: CategoryDao,
        orderDao: OrderDao
    ): RestaurantRepository {
        return RestaurantRepository(menuItemDao, categoryDao, orderDao)
    }
}
