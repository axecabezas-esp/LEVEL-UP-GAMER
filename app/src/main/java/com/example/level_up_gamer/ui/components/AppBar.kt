package com.example.level_up_gamer.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.level_up_gamer.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelUpAppBar(title: String) {
    TopAppBar(
        title = { Text(title, fontWeight = FontWeight.Bold) },
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.iconologo2),
                contentDescription = "Logo",
                modifier = Modifier.padding(start = 8.dp).size(50.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF212529),
            titleContentColor = Color(0xFF6CFF5A)
        )
    )
}
