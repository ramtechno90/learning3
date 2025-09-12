package com.example.restaurantmanager.ui.customer.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.restaurantmanager.data.local.model.CartItem
import com.example.restaurantmanager.ui.customer.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    onNavigateToCheckout: () -> Unit
) {
    val cartItems = cartViewModel.cartItems

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
                            Text("₹${cartViewModel.subtotal}")
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Parcel Charges:")
                            Text("₹${cartViewModel.parcelCharges}")
                        }
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Grand Total:", style = MaterialTheme.typography.titleLarge)
                            Text("₹${cartViewModel.grandTotal}", style = MaterialTheme.typography.titleLarge)
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
                    onDineInQuantityChanged = { quantity -> cartViewModel.onDineInQuantityChanged(cartItem, quantity) },
                    onTakeawayQuantityChanged = { quantity -> cartViewModel.onTakeawayQuantityChanged(cartItem, quantity) },
                    onInstructionsChanged = { instructions -> cartViewModel.onInstructionsChanged(cartItem, instructions) },
                    onRemoveItem = { cartViewModel.removeItem(cartItem) }
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
        FilledTonalIconButton(onClick = { onQuantityChanged(quantity - 1) }) {
            Icon(Icons.Filled.Remove, contentDescription = "Decrease quantity")
        }
        Text(
            text = quantity.toString(),
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
        FilledTonalIconButton(onClick = { onQuantityChanged(quantity + 1) }) {
            Icon(Icons.Default.Add, contentDescription = "Increase quantity")
        }
    }
}
