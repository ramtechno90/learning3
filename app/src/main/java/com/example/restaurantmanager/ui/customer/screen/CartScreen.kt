package com.example.restaurantmanager.ui.customer.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.restaurantmanager.data.local.model.CartItem
import com.example.restaurantmanager.ui.customer.viewmodel.CartViewModel

@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    onNavigateToCheckout: () -> Unit
) {
    val cartItems = cartViewModel.cartItems

    Scaffold(
        bottomBar = {
            if (cartItems.isNotEmpty()) {
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
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
            Text(text = cartItem.menuItem.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            // Dine-in quantity
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Dine-in:")
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { onDineInQuantityChanged(cartItem.dineInQuantity - 1) }) {
                    Text("-")
                }
                Text(cartItem.dineInQuantity.toString())
                IconButton(onClick = { onDineInQuantityChanged(cartItem.dineInQuantity + 1) }) {
                    Text("+")
                }
            }

            // Takeaway quantity
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Takeaway:")
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { onTakeawayQuantityChanged(cartItem.takeawayQuantity - 1) }) {
                    Text("-")
                }
                Text(cartItem.takeawayQuantity.toString())
                IconButton(onClick = { onTakeawayQuantityChanged(cartItem.takeawayQuantity + 1) }) {
                    Text("+")
                }
            }

            OutlinedTextField(
                value = cartItem.instructions,
                onValueChange = onInstructionsChanged,
                label = { Text("Special Instructions") },
                modifier = Modifier.fillMaxWidth()
            )

            TextButton(onClick = onRemoveItem) {
                Text("Remove")
            }
        }
    }
}
