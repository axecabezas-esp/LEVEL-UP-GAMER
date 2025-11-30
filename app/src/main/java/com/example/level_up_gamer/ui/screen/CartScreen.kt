package com.example.level_up_gamer.ui.screen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.level_up_gamer.data.model.Producto
import com.example.level_up_gamer.ui.components.LevelUpAppBar
import com.example.level_up_gamer.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(cartViewModel: CartViewModel = viewModel()) {
    val uiState by cartViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            LevelUpAppBar(title = "Tu Carrito")
        },
        containerColor = Color(0xFF1a1a1a),
        bottomBar = {
            // Sección fija abajo para el Total y el botón de Pagar
            if (uiState.productosEnCarrito.isNotEmpty()) {
                CartSummary(total = uiState.total, onPayClick = { cartViewModel.vaciarCarrito() })
            }
        }
    ) { paddingValues ->
        if (uiState.productosEnCarrito.isEmpty()) {
            EmptyCartMessage(modifier = Modifier.padding(paddingValues))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .testTag("listaCarrito"),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.productosEnCarrito) { producto ->
                    CartItemCard(
                        producto = producto,
                        onRemoveClick = { cartViewModel.removerProducto(producto) }
                    )
                }
            }
        }
    }
}

@Composable
fun CartItemCard(producto: Producto, onRemoveClick: () -> Unit) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF212529)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- LÓGICA HÍBRIDA DE IMAGEN (Igual que en Catalogo) ---
            Box(modifier = Modifier.size(80.dp).background(Color.Black, RoundedCornerShape(8.dp))) {
                if (producto.imagen.startsWith("http")) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(producto.imagen)
                            .crossfade(true)
                            .build(),
                        contentDescription = producto.nombre,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    val imageResId = context.resources.getIdentifier(
                        producto.imagen, "drawable", context.packageName
                    )
                    if (imageResId != 0) {
                        AsyncImage(
                            model = imageResId, // Coil acepta IDs de recursos directamente
                            contentDescription = producto.nombre,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            // Si falla la carga (por el error que tienes), mostrará el carrito gris
                            error = painterResource(android.R.drawable.ic_menu_report_image)
                        )
                    } else {
                        // CASO 3: No encontrado
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Imagen no disponible",
                            tint = Color.Gray,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    color = Color(0xFF6CFF5A),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = producto.precio,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }

            IconButton(onClick = onRemoveClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color(0xFFE63946) // Color rojo para eliminar
                )
            }
        }
    }
}

@Composable
fun CartSummary(total: String, onPayClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C3E50)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total a Pagar:", color = Color.White, fontSize = 20.sp)
                Text(total, color = Color(0xFF6CFF5A), fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onPayClick,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0DCAF0))
            ) {
                Text("Finalizar Compra", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }
    }
}

@Composable
fun EmptyCartMessage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Carrito Vacío",
            modifier = Modifier.size(100.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Tu carrito está vacío",
            fontSize = 20.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "¡Ve al catálogo y agrega tus juegos favoritos!",
            fontSize = 14.sp,
            color = Color.DarkGray
        )
    }
}