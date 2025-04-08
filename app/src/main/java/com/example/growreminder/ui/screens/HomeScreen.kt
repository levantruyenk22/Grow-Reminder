package com.example.growreminder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(userName: String = "Hygge") {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Xin chào $userName",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4B2992), // Màu tím đậm
            modifier = Modifier.padding(bottom = 24.dp)
        )

        MenuCard(title = "Phát triển bản thân")
        Spacer(modifier = Modifier.height(16.dp))
        MenuCard(title = "Lịch dự kiến")
        Spacer(modifier = Modifier.height(16.dp))
        MenuCard(title = "Lịch cần làm")
    }
}

@Composable
fun MenuCard(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xF0F0ECF6)) // Màu nền tím nhạt
            .padding(16.dp)
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4B2992), // Màu tím đậm
            modifier = Modifier.align(Alignment.CenterStart)
        )
    }
}

