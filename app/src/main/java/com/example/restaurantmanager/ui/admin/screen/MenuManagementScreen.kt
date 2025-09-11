package com.example.restaurantmanager.ui.admin.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restaurantmanager.data.local.model.Category
import com.example.restaurantmanager.data.local.model.MenuItem
import com.example.restaurantmanager.ui.admin.viewmodel.AdminMenuViewModel

@Composable
fun MenuManagementScreen(
    viewModel: AdminMenuViewModel = hiltViewModel()
) {
    val menuItems by viewModel.menuItems.collectAsState()
    val categories by viewModel.categories.collectAsState()

    var showAddMenuItemDialog by remember { mutableStateOf(false) }
    var editingMenuItem by remember { mutableStateOf<MenuItem?>(null) }
    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var editingCategory by remember { mutableStateOf<Category?>(null) }

    if (showAddMenuItemDialog || editingMenuItem != null) {
        AddEditMenuItemDialog(
            menuItem = editingMenuItem,
            categories = categories,
            onDismiss = {
                showAddMenuItemDialog = false
                editingMenuItem = null
            },
            onConfirm = {
                if (editingMenuItem == null) {
                    viewModel.addMenuItem(it)
                } else {
                    viewModel.updateMenuItem(it)
                }
                showAddMenuItemDialog = false
                editingMenuItem = null
            }
        )
    }

    if (showAddCategoryDialog || editingCategory != null) {
        AddEditCategoryDialog(
            category = editingCategory,
            onDismiss = {
                showAddCategoryDialog = false
                editingCategory = null
            },
            onConfirm = {
                if (editingCategory == null) {
                    viewModel.addCategory(it)
                } else {
                    viewModel.updateCategory(it)
                }
                showAddCategoryDialog = false
                editingCategory = null
            }
        )
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("Menu Management", style = MaterialTheme.typography.headlineMedium)
            Button(onClick = { showAddMenuItemDialog = true }) {
                Text("Add Menu Item")
            }
        }
        items(menuItems) { menuItem ->
            MenuItemManagementCard(
                menuItem = menuItem,
                onUpdate = viewModel::updateMenuItem,
                onDelete = viewModel::deleteMenuItem,
                onEdit = { editingMenuItem = it }
            )
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
            Text("Category Management", style = MaterialTheme.typography.headlineMedium)
            Button(onClick = { showAddCategoryDialog = true }) {
                Text("Add Category")
            }
        }
        items(categories) { category ->
            CategoryManagementCard(
                category = category,
                onDelete = viewModel::deleteCategory,
                onRename = { editingCategory = it }
            )
        }
    }
}

@Composable
fun MenuItemManagementCard(
    menuItem: MenuItem,
    onUpdate: (MenuItem) -> Unit,
    onDelete: (MenuItem) -> Unit,
    onEdit: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(menuItem.name, style = MaterialTheme.typography.titleMedium)
            Row {
                IconButton(onClick = onEdit) { Text("Edit") }
                IconButton(onClick = { onDelete(menuItem) }) { Text("Delete") }
                Switch(checked = menuItem.in_stock, onCheckedChange = { onUpdate(menuItem.copy(in_stock = it)) })
                Text("In Stock")
                Switch(checked = menuItem.takeaway_available, onCheckedChange = { onUpdate(menuItem.copy(takeaway_available = it)) })
                Text("Takeaway")
            }
        }
    }
}

@Composable
fun CategoryManagementCard(
    category: Category,
    onDelete: (Category) -> Unit,
    onRename: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(modifier = Modifier.padding(16.dp)) {
            Text(category.name, modifier = Modifier.weight(1f))
            IconButton(onClick = onRename) { Text("Rename") }
            IconButton(onClick = { onDelete(category) }) { Text("Delete") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditMenuItemDialog(
    menuItem: MenuItem?,
    categories: List<Category>,
    onDismiss: () -> Unit,
    onConfirm: (MenuItem) -> Unit
) {
    var name by remember { mutableStateOf(menuItem?.name ?: "") }
    var description by remember { mutableStateOf(menuItem?.description ?: "") }
    var price by remember { mutableStateOf(menuItem?.price?.toString() ?: "") }
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(menuItem?.category ?: categories.firstOrNull()?.name ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (menuItem == null) "Add Menu Item" else "Edit Menu Item") },
        text = {
            Column {
                TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                TextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
                TextField(value = price, onValueChange = { price = it }, label = { Text("Price") })
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        value = selectedCategory,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    selectedCategory = category.name
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val newMenuItem = menuItem?.copy(
                    name = name,
                    description = description,
                    price = price.toDouble(),
                    category = selectedCategory
                ) ?: MenuItem(
                    name = name,
                    description = description,
                    price = price.toDouble(),
                    category = selectedCategory
                )
                onConfirm(newMenuItem)
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AddEditCategoryDialog(
    category: Category?,
    onDismiss: () -> Unit,
    onConfirm: (Category) -> Unit
) {
    var name by remember { mutableStateOf(category?.name ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (category == null) "Add Category" else "Edit Category") },
        text = {
            TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
        },
        confirmButton = {
            Button(onClick = {
                val newCategory = category?.copy(name = name) ?: Category(name = name)
                onConfirm(newCategory)
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
