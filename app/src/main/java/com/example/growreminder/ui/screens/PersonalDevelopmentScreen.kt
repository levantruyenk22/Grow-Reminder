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
    MainAppLayout {
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

        Spacer(modifier = Modifier.height(80.dp))

        // ✅ Thay đổi cách bố trí khoảng cách ở đây
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            ChoiceCard("Học Tập, đọc sách") { navController.navigate("studyChoice") }
            // ✅ Thêm Spacer để giống trang DailyMotivationScreen
            Spacer(modifier = Modifier.height(70.dp))
            ChoiceCard("Sức khỏe") { navController.navigate("healthChoice") }
            // ✅ Thêm Spacer để giống trang DailyMotivationScreen
            Spacer(modifier = Modifier.height(70.dp))
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