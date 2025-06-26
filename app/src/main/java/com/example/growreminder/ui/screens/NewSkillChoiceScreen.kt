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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun NewSkillChoiceScreen(navController: NavController) {
    // ✅ Sử dụng MainAppLayout
    MainAppLayout {
        SkillTitleSection(
            title = "Gợi ý Kĩ năng mới",
            onBackClick = { navController.popBackStack() }
        )
        Spacer(modifier = Modifier.height(50.dp))

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                SkillCard(text = "Học ngoại ngữ", navController)
                SkillCard(text = "Học Giao tiếp", navController)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                SkillCard(text = "Học cách kiếm tiền", navController)
                SkillCard(text = "Học kỹ năng mới", navController)
            }
        }
    }
}

@Composable
fun SkillTitleSection(title: String, onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4A148C)
        )
    }
}

@Composable
fun SkillCard(text: String, navController: NavController) {
    Box(
        modifier = Modifier
            .size(170.dp)
            .shadow(8.dp, shape = RoundedCornerShape(16.dp))
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .clickable {
                navController.navigate("schedule/${text}")
            }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4A148C),
            textAlign = TextAlign.Center
        )
    }
}