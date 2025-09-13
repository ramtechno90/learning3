package com.example.restaurantmanager.ui.customer.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restaurantmanager.data.local.model.CartItem
import com.example.restaurantmanager.data.local.model.MenuItem
import com.example.restaurantmanager.ui.customer.viewmodel.CartViewModel
import com.example.restaurantmanager.ui.customer.viewmodel.OrderViewModel
import com.example.restaurantmanager.ui.theme.RestaurantManagerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderConfirmationScreen(
    orderViewModel: OrderViewModel = hiltViewModel(),
    cartViewModel: CartViewModel,
    onPlaceOrder: (Int) -> Unit
) {
    var customerName by remember { mutableStateOf("") }

    OrderConfirmationScreenContent(
        customerName = customerName,
        onCustomerNameChange = { customerName = it },
        cartItems = cartViewModel.cartItems,
        subtotal = cartViewModel.subtotal,
        parcelCharges = cartViewModel.parcelCharges,
        grandTotal = cartViewModel.grandTotal,
        onPlaceOrder = {
            orderViewModel.placeOrder(
                customerName = customerName,
                cartItems = cartViewModel.cartItems,
                total = cartViewModel.grandTotal,
                onOrderPlaced = { orderId ->
                    cartViewModel.clear()
                    onPlaceOrder(orderId)
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderConfirmationScreenContent(
    customerName: String,
    onCustomerNameChange: (String) -> Unit,
    cartItems: List<CartItem>,
    subtotal: Double,
    parcelCharges: Double,
    grandTotal: Double,
    onPlaceOrder: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Confirm Order") })
        },
        bottomBar = {
            Button(
                onClick = onPlaceOrder,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = customerName.isNotBlank() && cartItems.isNotEmpty()
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
                onValueChange = onCustomerNameChange,
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
                    cartItems.forEach { cartItem ->
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
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderConfirmationScreenPreview() {
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
        OrderConfirmationScreenContent(
            customerName = "John Doe",
            onCustomerNameChange = {},
            cartItems = cartItems,
            subtotal = 850.0,
            parcelCharges = 20.0,
            grandTotal = 870.0,
            onPlaceOrder = {}
        )
    }
}
