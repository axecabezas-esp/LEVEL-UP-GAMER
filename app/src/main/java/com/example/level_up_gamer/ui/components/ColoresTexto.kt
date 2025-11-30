package com.example.level_up_gamer.ui.components

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun textFieldColors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    unfocusedTextColor = Color.White,
    focusedTextColor = Color.White,
    cursorColor = Color(0xFF6CFF5A),
    unfocusedBorderColor = Color(0xFF6CFF5A),
    focusedBorderColor = Color(0xFF0DCAF0),
    unfocusedLabelColor = Color.Gray,
    focusedLabelColor = Color(0xFF0DCAF0),
    errorTextColor = Color.White,
    errorCursorColor = Color.Red,
    errorBorderColor = Color.Red,
    errorLabelColor = Color.Red,
    errorSupportingTextColor = Color.Red

)