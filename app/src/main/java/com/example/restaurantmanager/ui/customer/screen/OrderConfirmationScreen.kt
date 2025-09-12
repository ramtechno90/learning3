package com.example.restaurantmanager.ui.customer.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restaurantmanager.ui.customer.viewmodel.CartViewModel
import com.example.restaurantmanager.ui.customer.viewmodel.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderConfirmationScreen(
    orderViewModel: OrderViewModel = hiltViewModel(),
    cartViewModel: CartViewModel,
    onPlaceOrder: (Int) -> Unit
) {
    var customerName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Confirm Order") })
        },
        bottomBar = {
            Button(
                onClick = {
                    orderViewModel.placeOrder(
                        customerName = customerName,
                        cartItems = cartViewModel.cartItems,
                        total = cartViewModel.grandTotal,
                        onOrderPlaced = { orderId ->
                            cartViewModel.clear()
                            onPlaceOrder(orderId)
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = customerName.isNotBlank() && cartViewModel.cartItems.isNotEmpty()
            ) {
                Text("Place Order")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = customerName,
                onValueChange = { customerName = it },
                label = { Text("Your Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Order Summary", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    cartViewModel.cartItems.forEach { cartItem ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${cartItem.menuItem.name} x${cartItem.dineInQuantity + cartItem.takeawayQuantity}")
                            Text("₹${cartItem.menuItem.price * (cartItem.dineInQuantity + cartItem.takeawayQuantity)}")
                        }
                    }
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
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
                }
            }
        }
    }
}
