package com.example.restaurantmanager.ui.admin.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restaurantmanager.data.local.model.Order
import com.example.restaurantmanager.ui.admin.viewmodel.AdminOrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOrderDashboardScreen(
    viewModel: AdminOrderViewModel = hiltViewModel(),
    onNavigateToMenuManagement: () -> Unit,
    onLogout: () -> Unit
) {
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Pending", "Accepted", "Completed", "Rejected")

    val pendingOrders by viewModel.pendingOrders.collectAsState()
    val acceptedOrders by viewModel.acceptedOrders.collectAsState()
    val completedOrders by viewModel.completedOrders.collectAsState()
    val rejectedOrders by viewModel.rejectedOrders.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Dashboard") },
                actions = {
                    TextButton(onClick = onNavigateToMenuManagement) {
                        Text("Menu")
                    }
                    TextButton(onClick = onLogout) {
                        Text("Logout")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            TabRow(selectedTabIndex = tabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = tabIndex == index,
                        onClick = { tabIndex = index },
                        text = { Text(title) }
                    )
                }
            }
            when (tabIndex) {
                0 -> OrderList(orders = pendingOrders, onUpdateStatus = viewModel::updateOrderStatus)
                1 -> OrderList(orders = acceptedOrders, onUpdateStatus = viewModel::updateOrderStatus)
                2 -> OrderList(orders = completedOrders, onUpdateStatus = viewModel::updateOrderStatus, onClearAll = viewModel::clearCompletedOrders)
                3 -> OrderList(orders = rejectedOrders, onUpdateStatus = viewModel::updateOrderStatus, onClearAll = viewModel::clearRejectedOrders)
            }
        }
    }
}

@Composable
fun OrderList(
    orders: List<Order>,
    onUpdateStatus: (Order, String) -> Unit,
    onClearAll: (() -> Unit)? = null
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        if (onClearAll != null) {
            item {
                Button(onClick = onClearAll, modifier = Modifier.padding(16.dp)) {
                    Text("Clear All")
                }
            }
        }
        items(orders) { order ->
            OrderCard(order, onUpdateStatus)
        }
    }
}

@Composable
fun OrderCard(order: Order, onUpdateStatus: (Order, String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        BoxWithConstraints {
            val showVerticalLayout = maxWidth < 360.dp
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Order #${order.id}", style = MaterialTheme.typography.titleMedium)
                Text("Customer: ${order.customer_name}")
                Text("Total: ₹${order.total}")
                Text("Items: ${order.items}") // This will be a JSON string. A better implementation would parse it.
                if (showVerticalLayout) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        when (order.status) {
                            "Pending" -> {
                                Button(onClick = { onUpdateStatus(order, "Accepted") }) { Text("Accept") }
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(onClick = { onUpdateStatus(order, "Rejected") }) { Text("Reject") }
                            }
                            "Accepted" -> {
                                Button(onClick = { onUpdateStatus(order, "Completed") }) { Text("Complete") }
                            }
                        }
                    }
                } else {
                    Row {
                        when (order.status) {
                            "Pending" -> {
                                Button(onClick = { onUpdateStatus(order, "Accepted") }) { Text("Accept") }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(onClick = { onUpdateStatus(order, "Rejected") }) { Text("Reject") }
                            }
                            "Accepted" -> {
                                Button(onClick = { onUpdateStatus(order, "Completed") }) { Text("Complete") }
                            }
                        }
                    }
                }
            }
        }
    }
}
