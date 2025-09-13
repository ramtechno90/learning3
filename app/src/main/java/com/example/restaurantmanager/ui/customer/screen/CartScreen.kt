package com.example.restaurantmanager.ui.customer.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.restaurantmanager.data.local.model.CartItem
import com.example.restaurantmanager.data.local.model.MenuItem
import com.example.restaurantmanager.ui.customer.viewmodel.CartViewModel
import com.example.restaurantmanager.ui.theme.RestaurantManagerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    onNavigateToCheckout: () -> Unit
) {
    val cartItems = cartViewModel.cartItems

    CartScreenContent(
        cartItems = cartItems,
        subtotal = cartViewModel.subtotal,
        parcelCharges = cartViewModel.parcelCharges,
        grandTotal = cartViewModel.grandTotal,
        onNavigateToCheckout = onNavigateToCheckout,
        onDineInQuantityChanged = { cartItem, quantity -> cartViewModel.onDineInQuantityChanged(cartItem, quantity) },
        onTakeawayQuantityChanged = { cartItem, quantity -> cartViewModel.onTakeawayQuantityChanged(cartItem, quantity) },
        onInstructionsChanged = { cartItem, instructions -> cartViewModel.onInstructionsChanged(cartItem, instructions) },
        onRemoveItem = { cartViewModel.removeItem(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreenContent(
    cartItems: List<CartItem>,
    subtotal: Double,
    parcelCharges: Double,
    grandTotal: Double,
    onNavigateToCheckout: () -> Unit,
    onDineInQuantityChanged: (CartItem, Int) -> Unit,
    onTakeawayQuantityChanged: (CartItem, Int) -> Unit,
    onInstructionsChanged: (CartItem, String) -> Unit,
    onRemoveItem: (CartItem) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Your Cart") })
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Subtotal:")
                            Text("₹${subtotal}")
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Parcel Charges:")
                            Text("₹${parcelCharges}")
                        }
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Grand Total:", style = MaterialTheme.typography.titleLarge)
                            Text("₹${grandTotal}", style = MaterialTheme.typography.titleLarge)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onNavigateToCheckout,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Proceed to Checkout")
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(cartItems) { cartItem ->
                CartItemCard(
                    cartItem = cartItem,
                    onDineInQuantityChanged = { quantity -> onDineInQuantityChanged(cartItem, quantity) },
                    onTakeawayQuantityChanged = { quantity -> onTakeawayQuantityChanged(cartItem, quantity) },
                    onInstructionsChanged = { instructions -> onInstructionsChanged(cartItem, instructions) },
                    onRemoveItem = { onRemoveItem(cartItem) }
                )
            }
        }
    }
}

@Composable
fun CartItemCard(
    cartItem: CartItem,
    onDineInQuantityChanged: (Int) -> Unit,
    onTakeawayQuantityChanged: (Int) -> Unit,
    onInstructionsChanged: (String) -> Unit,
    onRemoveItem: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(text = cartItem.menuItem.name, style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = onRemoveItem) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove Item")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Dine-in quantity
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Dine-in:")
                QuantitySelector(
                    quantity = cartItem.dineInQuantity,
                    onQuantityChanged = onDineInQuantityChanged
                )
            }

            // Takeaway quantity
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Takeaway:")
                QuantitySelector(
                    quantity = cartItem.takeawayQuantity,
                    onQuantityChanged = onTakeawayQuantityChanged
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = cartItem.instructions,
                onValueChange = onInstructionsChanged,
                label = { Text("Special Instructions") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun QuantitySelector(
    quantity: Int,
    onQuantityChanged: (Int) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { onQuantityChanged(quantity - 1) }) {
            Text("-")
        }
        Text(
            text = quantity.toString(),
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
        IconButton(onClick = { onQuantityChanged(quantity + 1) }) {
            Text("+")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CartScreenPreview() {
    RestaurantManagerTheme {
        val cartItems = listOf(
            CartItem(
                menuItem = MenuItem(id = 1, name = "Paneer Tikka", description = "Grilled cottage cheese cubes marinated in spices", price = 250.0, category = "Starters", in_stock = true),
                dineInQuantity = 1,
                takeawayQuantity = 1,
                instructions = "Make it spicy"
            ),
            CartItem(
                menuItem = MenuItem(id = 3, name = "Dal Makhani", description = "Creamy black lentils cooked with butter and spices", price = 300.0, category = "Main Course", in_stock = true),
                dineInQuantity = 2,
                takeawayQuantity = 0,
                instructions = ""
            )
        )
        CartScreenContent(
            cartItems = cartItems,
            subtotal = 850.0,
            parcelCharges = 20.0,
            grandTotal = 870.0,
            onNavigateToCheckout = {},
            onDineInQuantityChanged = { _, _ -> },
            onTakeawayQuantityChanged = { _, _ -> },
            onInstructionsChanged = { _, _ -> },
            onRemoveItem = {}
        )
    }
}
