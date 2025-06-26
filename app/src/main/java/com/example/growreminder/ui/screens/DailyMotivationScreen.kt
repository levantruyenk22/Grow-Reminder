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

    // 1. Chỉ cần gọi MainAppLayout để có nền động
    MainAppLayout {
        // 2. Đặt toàn bộ nội dung gốc của bạn vào đây.
        //    Lưu ý: Chúng ta đã xóa Column gốc vì MainAppLayout đã cung cấp sẵn.

        // Nút Back + Tiêu đề
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 26.dp)
        ) {
            // Bạn có thể cân nhắc làm cho nút Back này đẹp hơn giống như phiên bản
            // trong mã gốc có hiệu ứng của bạn để giao diện được nhất quán
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF1565C0) // Thay đổi màu để hợp với nền xanh mới
                )
            }
            Text(
                text = "Chào bạn đến với sự kỉ luật",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1565C0), // Thay đổi màu để hợp với nền xanh mới
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(70.dp))

        // Các mục giống Card
        // Có thể bạn cũng muốn cập nhật màu sắc của các Card này
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


@Composable
fun MotivationOptionCard(text: String, onClick: () -> Unit) {
    // Để card nổi bật hơn trên nền xanh, chúng ta có thể làm nó sáng hơn
    // và thêm một chút bóng đổ.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp)
            // Sử dụng màu trắng hoặc màu rất nhạt để nổi bật
            .background(Color.White.copy(alpha = 0.85f), shape = RoundedCornerShape(30.dp))
            .clickable { onClick() }
            .padding(horizontal = 30.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1565C0) // Màu chữ đậm để dễ đọc
        )
    }
}