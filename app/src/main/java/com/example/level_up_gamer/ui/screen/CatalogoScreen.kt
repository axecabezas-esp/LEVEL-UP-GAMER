package com.example.level_up_gamer.ui.screen

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.level_up_gamer.R
import com.example.level_up_gamer.model.Producto
import com.example.level_up_gamer.ui.components.LevelUpAppBar
import com.example.level_up_gamer.viewmodel.CatalogoViewModel
import com.example.level_up_gamer.viewmodel.CatalogoUiState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CatalogoScreen(catalogoViewModel: CatalogoViewModel = viewModel()) {
    val uiState by catalogoViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            LevelUpAppBar(title = "Catálogo de Productos")
        },
        containerColor = Color(0xFF1a1a1a)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            item {
                Text(
                    text = "🕹️ Nuestros Productos",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6CFF5A),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth()
                )
            }

            uiState.categorias.forEach { categoria ->
                if (categoria.nombre != "Todos") {
                    item {
                        Text(
                            text = categoria.nombre,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0DCAF0),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth()
                        )
                    }
                    items(categoria.productos) { producto ->
                        ProductCard(producto = producto)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(producto: Producto) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF212529))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val imageResId = context.getDrawableResourceId(name = producto.imagen)
            if (imageResId != null) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = producto.nombre,
                    modifier = Modifier.height(120.dp).fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(text = producto.nombre, fontWeight = FontWeight.Bold, color = Color(0xFF6CFF5A), textAlign = TextAlign.Center, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = producto.descripcion, color = Color.LightGray, textAlign = TextAlign.Center, modifier = Modifier.heightIn(min = 40.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Precio: ${producto.precio}", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {  },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0DCAF0))
            ) {
                Text("Agregar al Carrito")
            }
        }
    }
}

private fun Context.getDrawableResourceId(name: String): Int? {
    val resourceId = this.resources.getIdentifier(name, "drawable", this.packageName)
    return if (resourceId == 0) null else resourceId
}
