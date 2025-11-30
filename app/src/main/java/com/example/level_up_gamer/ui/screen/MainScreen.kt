package com.example.level_up_gamer.ui.screen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.level_up_gamer.data.model.Noticia
import com.example.level_up_gamer.ui.components.LevelUpAppBar
import com.example.level_up_gamer.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(mainViewModel: MainViewModel = viewModel()) {
    val uiState by mainViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            LevelUpAppBar(title = "Inicio")
        },
        containerColor = Color(0xFF1a1a1a)
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().testTag("listaNoticias")
            ) {
                item { Header() }
                item { SectionTitle("Noticias Importantes") }
                items(uiState.noticias) { noticia ->
                    NoticiaCard(noticia = noticia)
                }
            }
        }
    }
}

@Composable
fun Header() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "LEVEL-UP GAMER",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6CFF5A)
        )
        Text(
            "Bienvenido al Mundo Gamer 🎮",
            fontSize = 18.sp,
            color = Color(0xFF0DCAF0)
        )
    }
}

@Composable
fun NoticiaCard(noticia: Noticia) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF212529))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val imageResId = context.getDrawableResourceId(noticia.imagen)
            if (imageResId != null) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = noticia.titulo,
                    modifier = Modifier.size(120.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = noticia.titulo,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6CFF5A),
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = noticia.contenido,
                color = Color.White,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF0DCAF0),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        textAlign = TextAlign.Center
    )
}



private fun Context.getDrawableResourceId(name: String): Int? {
    val resourceId = this.resources.getIdentifier(name, "drawable", this.packageName)
    return if (resourceId == 0) null else resourceId
}
