package com.example.growreminder.ui.screens

import android.R.attr.color
import android.R.color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.growreminder.Sign_in.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,  // Accept navController
    viewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory),
    onSignInSuccess: () -> Unit = {}
) {
    val context = LocalContext.current
    val user by viewModel.user.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val signInError by viewModel.signInError.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Handle sign-in success
    LaunchedEffect(user) {
        user?.let {
            onSignInSuccess()
        }
    }

    LaunchedEffect(signInError) {
        signInError?.let { error ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(error)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp)
                )
            } else {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    if (user == null) {
                        SignInContent(
                            onSignInClick = { viewModel.signIn() },
                        )
                    } else {
                        UserProfileContent(
                            user = user,
                            onSignOutClick = { viewModel.signOut() },
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SignInContent(
    onSignInClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxHeight()
            .background(color = Color.White)
    ) {
        Text(
            text = "Welcome to",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top=100.dp),
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF4A148C)
        )
        Text(
            text = "GrowReminder",
            fontWeight = FontWeight.Bold,
            fontSize = 35.sp,
            color = Color(0xFF4A148C)
        )

        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
                .padding(bottom = 200.dp)
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom=150.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "Tôi ở đây để cho bạn sự KỈ LUẬT. Bạn đã sẵn sàng cho sự thày đổi chưa?",
                    color = Color(0xFF5E35B1),
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Button(
                onClick = onSignInClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A148C),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .width(500.dp)
                    .height(75.dp)
                    .padding(start = 30.dp, end = 30.dp)
            ) {
                    Text(
                        text = " SIGN IN WITH GOOGLE",
                        fontSize = 20.sp,
                    )
                }
            }
        }
    }



@Composable
private fun UserProfileContent(
    user: com.google.firebase.auth.FirebaseUser?,
    onSignOutClick: () -> Unit,
    navController: NavController
) {
    val profilePainter = rememberAsyncImagePainter(
        model = user?.photoUrl
    )

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(Modifier.fillMaxWidth().padding(bottom = 100.dp)) {
            Button(
                onClick = { navController.navigate("Profile") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF4A148C)
                ),
                modifier = Modifier
                    .align(Alignment.TopEnd)
            ) {
                Text(
                    text = "Profile",
                    fontSize = 25.sp
                )
            }

            Button(
                onClick = onSignOutClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF4A148C)
                ),
                modifier = Modifier
                    .align(Alignment.TopStart)
            ) {
                Text(
                    text = "Sign Out",
                    fontSize = 25.sp
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            AsyncImage(
                model = user?.photoUrl,
                contentDescription = "Profile Picture",
                placeholder = profilePainter,
                fallback = profilePainter,
                modifier = Modifier
                    .size(250.dp)
                    .clip(CircleShape)
            )

            Text(
                text = "Welcome, ${user?.displayName ?: "User"}",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 10.dp),
                textAlign = TextAlign.Center
                )

            Text(
                text = user?.email ?: "",
                fontSize = 20.sp,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        Box(Modifier.fillMaxSize()){
            Button(
                onClick = {navController.navigate("PhatTrien")},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A148C),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .height(50.dp)
                    .width(350.dp)
                    .padding(start = 15.dp, end = 15.dp)
            ) {
                Text(
                    text = "Get Started",
                    fontSize = 25.sp,
                )
            }
        }
    }
}
