package com.example.growreminder.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.growreminder.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HeaderProfile(navHostController: NavHostController, detail: String){
    Column(
        modifier = Modifier.background(Color.White)
    ) {
        Box {
            Button(
                onClick = {navHostController.popBackStack()},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF42AFFF)
                ),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 40.dp, start = 5.dp, end = 15.dp, bottom = 15.dp)
            )
            {
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
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, top = 55.dp)
            )
        }
    }
}

@Composable
fun ProfileScreen(navController: NavHostController) {

    val user = FirebaseAuth.getInstance().currentUser
    var userName by remember { mutableStateOf(user?.displayName ?: "") }
    var userEmail = user?.email ?: "No email available"
    val imageUrl = user?.photoUrl?.toString() ?: ""

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HeaderProfile(navHostController = navController, detail = "Profile")
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 120.dp)
                    .background(color = Color.White)
            ) {
                Box(modifier = Modifier.fillMaxHeight()) {
                    if (imageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "User Avatar",
                            modifier = Modifier
                                .size(200.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = "Default Avatar",
                            modifier = Modifier
                                .size(200.dp)
                                .clip(CircleShape)
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.camera_image),
                        contentDescription = "Camera Icon",
                        modifier = Modifier
                            .size(40.dp)
                    )
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Name",
                fontWeight = FontWeight.Bold,
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
                fontWeight = FontWeight.Bold,
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
        }
    }
}
