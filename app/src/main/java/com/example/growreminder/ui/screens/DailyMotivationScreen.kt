package com.example.growreminder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun DailyMotivationScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Nút Back + Tiêu đề
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 26.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF4A148C)
                )
            }
            Text(
                text = "Chào bạn đến với sự kỉ luật",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A148C),
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(70.dp))

        // Các mục giống Card
        MotivationOptionCard("Phát triển bản thân") {
            navController.navigate("personalDevelopment")
        }
        Spacer(modifier = Modifier.height(70.dp))

        MotivationOptionCard("Lịch dự kiến") {
            navController.navigate("schedule")
        }
        Spacer(modifier = Modifier.height(70.dp))

        MotivationOptionCard("Lịch cần làm") {
            navController.navigate("schedule_list")
        }
    }
}

// ✅ Đổi tên hàm để tránh trùng lặp
@Composable
fun MotivationOptionCard(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp)
            .background(Color(0xFFF1EDF9), shape = RoundedCornerShape(30.dp))
            .clickable { onClick() }
            .padding(horizontal = 30.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF4A148C)
        )
    }
}
