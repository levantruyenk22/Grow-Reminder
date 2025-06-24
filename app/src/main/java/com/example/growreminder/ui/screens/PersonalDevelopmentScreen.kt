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
import com.example.growreminder.sign_in.AuthViewModel

@Composable
fun PersonalDevelopmentScreen(navController: NavController, authViewModel: AuthViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 30.dp),
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
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A148C),
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(80.dp)) // Khoảng cách sau tiêu đề

        // Các lựa chọn với khoảng cách đều
        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            ChoiceCard("Học Tập, đọc sách") { navController.navigate("studyChoice") }
            ChoiceCard("Sức khỏe") { navController.navigate("healthChoice") }
            ChoiceCard("Kỹ năng mới") { navController.navigate("newSkillChoice") }
        }
    }
}

@Composable
fun ChoiceCard(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(Color(0xFFF1EDF9), shape = RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 35.dp),
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
