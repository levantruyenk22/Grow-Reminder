package com.example.growreminder.ui.screens

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Một layout chung có thể tái sử dụng cho tất cả các màn hình trong ứng dụng.
 * Nó cung cấp một background gradient chuyển động mượt mà.
 *
 * @param content Nội dung Composable cụ thể của từng màn hình sẽ được đặt vào đây.
 */
@Composable
fun MainAppLayout(
    content: @Composable ColumnScope.() -> Unit
) {
    // Animation cho background gradient với hiệu ứng mượt mà
    val infiniteTransition = rememberInfiniteTransition(label = "background_transition")

    // Tạo các màu background chuyển động nhẹ nhàng
    val backgroundColors = listOf(
        infiniteTransition.animateColor(
            initialValue = Color(0xFFE3F2FD), // Xanh rất nhạt
            targetValue = Color(0xFFBBDEFB), // Xanh nhạt hơn
            animationSpec = infiniteRepeatable(
                animation = tween(4000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "bgColor1"
        ),
        infiniteTransition.animateColor(
            initialValue = Color(0xFFBBDEFB),
            targetValue = Color(0xFF90CAF9),
            animationSpec = infiniteRepeatable(
                animation = tween(5000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "bgColor2"
        ),
        infiniteTransition.animateColor(
            initialValue = Color(0xFF90CAF9),
            targetValue = Color(0xFFE1F5FE),
            animationSpec = infiniteRepeatable(
                animation = tween(4500, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "bgColor3"
        ),
        infiniteTransition.animateColor(
            initialValue = Color(0xFFE1F5FE),
            targetValue = Color(0xFFF0F8FF), // AliceBlue
            animationSpec = infiniteRepeatable(
                animation = tween(3500, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "bgColor4"
        )
    )

    // Gradient background với animation mượt mà
    val backgroundGradient = Brush.verticalGradient(
        colors = backgroundColors.map { it.value }
    )

    // Column chính làm layout gốc cho mọi màn hình
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.Start,
        content = content // Đặt nội dung của màn hình vào đây
    )
}