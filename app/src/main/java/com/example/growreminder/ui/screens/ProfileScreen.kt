package com.example.growreminder.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.growreminder.R
import com.example.growreminder.sign_in.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HeaderProfile(navHostController: NavHostController, detail: String, onLogout: () -> Unit) {
    Column(
        modifier = Modifier.background(Color.White)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { navHostController.popBackStack() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF42AFFF)
                ),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 40.dp, start = 5.dp, end = 15.dp, bottom = 15.dp)
            ) {
                Text(
                    text = "<",
                    fontSize = 40.sp
                )
            }

            Text(
                text = detail,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                color = Color(0xFF42AFFF),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 55.dp)
            )

            TextButton(
                onClick = onLogout,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 50.dp, end = 15.dp)
            ) {
                Text("Logout", color = Color.Red, fontSize = 25.sp, fontWeight = Bold)
            }
        }
    }
}

@Composable
fun ProfileScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    val user = FirebaseAuth.getInstance().currentUser
    var userName by remember { mutableStateOf(user?.displayName ?: "") }
    var userEmail by remember { mutableStateOf(user?.email ?: "No email available") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HeaderProfile(
                navHostController = navController,
                detail = "Profile",
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                }
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 120.dp)
                    .background(Color.White)
            ) {
                Box(modifier = Modifier.fillMaxHeight()) {
                    Image(
                        painter = painterResource(id = R.drawable.avatar),
                        contentDescription = "User Avatar",
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape)
                    )
                }
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Name",
                fontSize = 25.sp,
                modifier = Modifier.padding(top = 380.dp, start = 30.dp)
            )

            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Email",
                fontSize = 25.sp,
                modifier = Modifier.padding(start = 30.dp)
            )

            OutlinedTextField(
                value = userEmail,
                onValueChange = { userEmail = it },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ✅ Nút Go to Home
            Button(
                onClick = {
                    navController.navigate("home")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(text = "Get Started", fontSize = 18.sp)
            }
        }
    }
}
