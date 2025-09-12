package com.example.restaurantmanager.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.restaurantmanager.data.local.AppDatabase
import com.example.restaurantmanager.data.local.dao.CategoryDao
import com.example.restaurantmanager.data.local.dao.MenuItemDao
import com.example.restaurantmanager.data.local.dao.OrderDao
import com.example.restaurantmanager.data.local.model.Category
import com.example.restaurantmanager.data.local.model.MenuItem
import com.example.restaurantmanager.data.repository.RestaurantRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        provider: Provider<RoomDatabase.Callback>
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
        dbProvider: Provider<AppDatabase>
    ): RoomDatabase.Callback {
        return object : RoomDatabase.Callback() {
            override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                super.onCreate(db)
                val database = dbProvider.get()
                CoroutineScope(Dispatchers.IO).launch {
                    // Add Categories
                    val categoryDao = database.categoryDao()
                    categoryDao.insert(Category(name = "Starters"))
                    categoryDao.insert(Category(name = "Main Course"))
                    categoryDao.insert(Category(name = "Desserts"))
                    categoryDao.insert(Category(name = "Beverages"))

                    // Add Menu Items
                    val menuItemDao = database.menuItemDao()
                    // Starters
                    menuItemDao.insert(MenuItem(name = "Paneer Tikka", description = "Grilled cottage cheese cubes", price = 250.0, category = "Starters"))
                    menuItemDao.insert(MenuItem(name = "Veg Seekh Kebab", description = "Minced vegetables on skewers", price = 220.0, category = "Starters"))
                    menuItemDao.insert(MenuItem(name = "Chicken 65", description = "Spicy fried chicken", price = 280.0, category = "Starters"))

                    // Main Course
                    menuItemDao.insert(MenuItem(name = "Chicken Biryani", description = "Aromatic rice dish with chicken", price = 350.0, category = "Main Course"))
                    menuItemDao.insert(MenuItem(name = "Palak Paneer", description = "Cottage cheese in spinach gravy", price = 300.0, category = "Main Course"))
                    menuItemDao.insert(MenuItem(name = "Mutton Rogan Josh", description = "Aromatic lamb curry", price = 450.0, category = "Main Course"))

                    // Desserts
                    menuItemDao.insert(MenuItem(name = "Gulab Jamun", description = "Sweet milk dumplings", price = 100.0, category = "Desserts"))
                    menuItemDao.insert(MenuItem(name = "Rasmalai", description = "Spongy cheese dumplings in milk", price = 120.0, category = "Desserts"))
                    menuItemDao.insert(MenuItem(name = "Gajar Ka Halwa", description = "Carrot pudding", price = 150.0, category = "Desserts"))

                    // Beverages
                    menuItemDao.insert(MenuItem(name = "Coke", description = "Cold drink", price = 40.0, category = "Beverages", parcel_charge = 5.0))
                    menuItemDao.insert(MenuItem(name = "Fresh Lime Soda", description = "Refreshing lemon drink", price = 60.0, category = "Beverages"))
                    menuItemDao.insert(MenuItem(name = "Masala Chai", description = "Spiced tea", price = 50.0, category = "Beverages"))
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
