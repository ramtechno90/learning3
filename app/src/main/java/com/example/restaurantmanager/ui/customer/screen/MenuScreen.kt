package com.example.restaurantmanager.ui.customer.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restaurantmanager.data.local.model.MenuItem
import com.example.restaurantmanager.ui.customer.viewmodel.CartViewModel
import com.example.restaurantmanager.ui.customer.viewmodel.MenuViewModel
import com.example.restaurantmanager.ui.theme.RestaurantManagerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    menuViewModel: MenuViewModel = hiltViewModel(),
    cartViewModel: CartViewModel,
    onNavigateToCart: () -> Unit,
    onNavigateToAdmin: () -> Unit
) {
    val menuItems by menuViewModel.menuItems.collectAsState()
    val cartItemCount = cartViewModel.cartItems.size

    MenuScreenContent(
        menuItems = menuItems,
        cartItemCount = cartItemCount,
        onNavigateToCart = onNavigateToCart,
        onNavigateToAdmin = onNavigateToAdmin,
        onAddToCart = { cartViewModel.addItem(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreenContent(
    menuItems: Map<String, List<MenuItem>>,
    cartItemCount: Int,
    onNavigateToCart: () -> Unit,
    onNavigateToAdmin: () -> Unit,
    onAddToCart: (MenuItem) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Menu") },
                actions = {
                    TextButton(onClick = onNavigateToAdmin) {
                        Text("Admin")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToCart) {
                BadgedBox(badge = { Badge { Text(cartItemCount.toString()) } }) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "Shopping Cart")
                }
            }
        }
    ) { paddingValues ->
        val expandedCategories = remember { mutableStateMapOf<String, Boolean>() }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            menuItems.forEach { (category, items) ->
                val isExpanded = expandedCategories.getOrPut(category) { true }
                item {
                    CategoryHeader(
                        category = category,
                        isExpanded = isExpanded,
                        onToggle = { expandedCategories[category] = !isExpanded }
                    )
                }
                if (isExpanded) {
                    items(items) { menuItem ->
                        MenuItemCard(menuItem, onAddToCart = {
                            onAddToCart(menuItem)
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryHeader(category: String, isExpanded: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = category, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
        Icon(
            imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
            contentDescription = if (isExpanded) "Collapse" else "Expand"
        )
    }
}

@Composable
fun MenuItemCard(menuItem: MenuItem, onAddToCart: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .animateContentSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    ) {
        BoxWithConstraints {
            val showVerticalLayout = maxWidth < 360.dp
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = menuItem.name, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = menuItem.description, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                if (showVerticalLayout) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(text = "₹${menuItem.price}", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        FilledTonalButton(
                            onClick = onAddToCart,
                            enabled = menuItem.in_stock
                        ) {
                            Text(text = if (menuItem.in_stock) "Add to Cart" else "Out of Stock")
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "₹${menuItem.price}", style = MaterialTheme.typography.bodyLarge)
                        FilledTonalButton(
                            onClick = onAddToCart,
                            enabled = menuItem.in_stock
                        ) {
                            Text(text = if (menuItem.in_stock) "Add to Cart" else "Out of Stock")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    RestaurantManagerTheme {
        val menuItems = mapOf(
            "Starters" to listOf(
                MenuItem(id = 1, name = "Paneer Tikka", description = "Grilled cottage cheese cubes marinated in spices", price = 250.0, category = "Starters", in_stock = true),
                MenuItem(id = 2, name = "Chilli Potato", description = "Crispy fried potatoes tossed in a spicy sauce", price = 180.0, category = "Starters", in_stock = true)
            ),
            "Main Course" to listOf(
                MenuItem(id = 3, name = "Dal Makhani", description = "Creamy black lentils cooked with butter and spices", price = 300.0, category = "Main Course", in_stock = true),
                MenuItem(id = 4, name = "Shahi Paneer", description = "Cottage cheese cubes in a rich and creamy gravy", price = 350.0, category = "Main Course", in_stock = false)
            )
        )
        MenuScreenContent(
            menuItems = menuItems,
            cartItemCount = 2,
            onNavigateToCart = {},
            onNavigateToAdmin = {},
            onAddToCart = {}
        )
    }
}
