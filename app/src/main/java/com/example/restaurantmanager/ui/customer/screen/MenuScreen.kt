package com.example.restaurantmanager.ui.customer.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restaurantmanager.data.local.model.MenuItem
import com.example.restaurantmanager.ui.customer.viewmodel.CartViewModel
import com.example.restaurantmanager.ui.customer.viewmodel.MenuViewModel

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Menu") },
                actions = {
                    IconButton(onClick = onNavigateToAdmin) {
                        Icon(Icons.Filled.AdminPanelSettings, contentDescription = "Admin Panel")
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
                            cartViewModel.addItem(menuItem)
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
            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
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
