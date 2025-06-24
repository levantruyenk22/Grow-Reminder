package com.example.growreminder.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun CustomBottomBar(
    modifier: Modifier = Modifier,
    onFabClick: () -> Unit = {},
    onNavClick: (String) -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(115.dp) // tăng chiều cao một chút
            .navigationBarsPadding() // ✅ đẩy lên khỏi thanh hệ thống
            .padding(bottom = 0.dp), // ✅ thêm khoảng cách nhỏ để FAB không bị dính sát
        contentAlignment = Alignment.Center
    ) {
        // Background bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = Color(0xFFF3EDFF),
            tonalElevation = 4.dp,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val iconTint = Color(0xFF7E57C2)

                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = iconTint,
                    modifier = Modifier.clickable { onNavClick("home") }
                )
                Icon(
                    imageVector = Icons.Default.Event,
                    contentDescription = "Calendar",
                    tint = iconTint,
                    modifier = Modifier.clickable { onNavClick("calendar") }
                )
                Spacer(modifier = Modifier.width(56.dp)) // chừa chỗ cho FAB
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = "Docs",
                    tint = iconTint,
                    modifier = Modifier.clickable { onNavClick("docs") }
                )
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = "People",
                    tint = iconTint,
                    modifier = Modifier.clickable { onNavClick("people") }
                )
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = onFabClick,
            containerColor = Color(0xFF7E57C2),
            contentColor = Color.White,
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.TopCenter)
                .zIndex(1f), // đảm bảo nằm trên bar
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }
}
