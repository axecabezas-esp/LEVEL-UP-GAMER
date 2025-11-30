package com.example.level_up_gamer.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.level_up_gamer.data.model.Producto
import com.example.level_up_gamer.ui.components.LevelUpAppBar
import com.example.level_up_gamer.viewmodel.CatalogoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductosScreen(
    catalogoViewModel: CatalogoViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by catalogoViewModel.uiState.collectAsState()
    val productosLocales = remember(uiState) {
        uiState.categorias.flatMap { it.productos }
    }
    var productoAEditar by remember { mutableStateOf<Producto?>(null) }

    Scaffold(
        topBar = { LevelUpAppBar(title = "Administrar Productos") },
        bottomBar = {
            Button(
                onClick = onNavigateBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDC3545) // Un color rojo/rosado para indicar "Salir"
                )
            ) {
                Text("Volver al Menú", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = Color(0xFF1a1a1a)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Toca un producto para editarlo:",
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            items(productosLocales) { producto ->
                AdminProductItem(producto) {
                    productoAEditar = producto
                }
            }
        }
        if (productoAEditar != null) {
            EditarProductoDialog(
                producto = productoAEditar!!,
                onDismiss = { productoAEditar = null },
                onConfirm = { prodEditado ->
                    catalogoViewModel.editarProductoLocal(prodEditado)
                    productoAEditar = null
                }
            )
        }
    }
}

@Composable
fun AdminProductItem(producto: Producto, onClick: () -> Unit) {
    val context = LocalContext.current
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF212529))
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageResId = context.resources.getIdentifier(producto.imagen, "drawable", context.packageName)
            if (imageResId != 0) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(producto.nombre, color = Color.White, fontWeight = FontWeight.Bold)
                Text(producto.precio, color = Color(0xFF6CFF5A))
            }
            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.Gray)
        }
    }
}

@Composable
fun EditarProductoDialog(
    producto: Producto,
    onDismiss: () -> Unit,
    onConfirm: (Producto) -> Unit
) {
    var nombre by remember { mutableStateOf(producto.nombre) }
    var precio by remember { mutableStateOf(producto.precio) }
    var descripcion by remember { mutableStateOf(producto.descripcion) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Producto") },
        text = {
            Column {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val nuevoProducto = producto.copy(
                        nombre = nombre,
                        precio = precio,
                        descripcion = descripcion
                    )
                    onConfirm(nuevoProducto)
                }
            ) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}