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
fun PersonalDevelopmentScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Nút trở về và tiêu đề
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF4A148C)
                )
            }
            Text(
                text = "Phát triển bản thân",
                fontSize = 25.sp, // Tăng kích thước chữ
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A148C),
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(90.dp)) // Tăng khoảng cách với tiêu đề

        // Các mục chọn với khoảng cách rộng hơn
        ChoiceCard("Học Tập, đọc sách") { navController.navigate("studyChoice") }
        Spacer(modifier = Modifier.height(90.dp))
        ChoiceCard("Sức khỏe") { navController.navigate("healthChoice") }
        Spacer(modifier = Modifier.height(90.dp))
        ChoiceCard("Kỹ năng mới") { navController.navigate("NewSkillChoice") }
    }
}

@Composable
fun ChoiceCard(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp) // Tăng chiều cao
            .background(Color(0xFFF1EDF9), shape = RoundedCornerShape(30.dp)) // Tăng bo góc
            .clickable { onClick() }
            .padding(horizontal = 30.dp), // Tăng padding ngang
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            fontSize = 25.sp, // Tăng kích thước chữ
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF4A148C)
        )
    }
}

