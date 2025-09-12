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

import com.example.restaurantmanager.data.local.model.Category
import com.example.restaurantmanager.data.local.model.MenuItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        provider: Provider<AppDatabase.Callback>
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "restaurant_manager.db"
        )
            .addCallback(provider.get())
            .build()
    }

    @Provides
    @Singleton
    fun provideDatabaseCallback(
        db: Provider<AppDatabase>
    ): AppDatabase.Callback {
        return object : AppDatabase.Callback() {
            override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                super.onCreate(db)
                val database = db.get()
                CoroutineScope(Dispatchers.IO).launch {
                    // Add Categories
                    val categoryDao = database.categoryDao()
                    categoryDao.insert(Category(name = "Starters"))
                    categoryDao.insert(Category(name = "Main Course"))
                    categoryDao.insert(Category(name = "Desserts"))
                    categoryDao.insert(Category(name = "Beverages"))

                    // Add Menu Items
                    val menuItemDao = database.menuItemDao()
                    menuItemDao.insert(MenuItem(name = "Paneer Tikka", description = "Grilled cottage cheese cubes", price = 250.0, category = "Starters"))
                    menuItemDao.insert(MenuItem(name = "Chicken Biryani", description = "Aromatic rice dish with chicken", price = 350.0, category = "Main Course"))
                    menuItemDao.insert(MenuItem(name = "Gulab Jamun", description = "Sweet milk dumplings", price = 100.0, category = "Desserts"))
                    menuItemDao.insert(MenuItem(name = "Coke", description = "Cold drink", price = 40.0, category = "Beverages", parcel_charge = 5.0))
                }
            }
        }
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
