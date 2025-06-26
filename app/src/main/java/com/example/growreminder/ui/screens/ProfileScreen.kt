package com.example.growreminder.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.growreminder.R
import com.example.growreminder.sign_in.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    val user = FirebaseAuth.getInstance().currentUser
    var profileInfo by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    LaunchedEffect(Unit) {
        user?.let {
            Firebase.firestore.collection("users").document(it.uid).get()
                .addOnSuccessListener { doc ->
                    profileInfo = doc.data?.mapValues { it.value.toString() } ?: emptyMap()
                }
        }
    }

    // ✅ Sử dụng MainAppLayout
    MainAppLayout {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text("Hồ sơ cá nhân", fontWeight = FontWeight.Bold)
                    },
                    // ✅ Đặt màu nền trong suốt
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            // ✅ Đặt màu nền trong suốt
            containerColor = Color.Transparent
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(), // fillMaxSize để chiếm hết không gian Scaffold
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar
                Image(
                    painter = painterResource(id = R.drawable.avatar),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Họ tên
                Text(
                    text = profileInfo["fullName"] ?: "Họ và tên",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                // Username
                Text(
                    text = "@${profileInfo["username"] ?: "username"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Ngày sinh
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("🎂 Ngày sinh: ", fontWeight = FontWeight.Medium)
                    Text(profileInfo["birthdate"] ?: "Chưa cập nhật")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Nơi ở
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("📍 Nơi ở: ", fontWeight = FontWeight.Medium)
                    Text(profileInfo["location"] ?: "Chưa cập nhật")
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Nút Cập nhật thông tin
                Button(
                    onClick = { navController.navigate("update_info") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cập nhật thông tin")
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Nút Get Started
                Button(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Bắt đầu sử dụng", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.weight(1f))

                // Nút Đăng xuất
                OutlinedButton(
                    onClick = {
                        authViewModel.signOut()
                        navController.navigate("login") { popUpTo(0) }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Red
                    ),
                    border = BorderStroke(1.dp, Color.Red)
                ) {
                    Text("Đăng xuất", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}