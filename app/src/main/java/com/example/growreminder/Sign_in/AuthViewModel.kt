package com.example.growreminder.sign_in

import android.content.Context
import android.util.Log
import androidx.credentials.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import androidx.credentials.exceptions.NoCredentialException
import androidx.credentials.exceptions.GetCredentialException

sealed class AuthState {
    object Unauthenticated : AuthState()
    object Authenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}

data class AuthUiState(
    val fullName: String = "",
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val birthdate: String = "",
    val location: String = ""
)

class AuthViewModel(application: android.app.Application) : AndroidViewModel(application) {

    private val auth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState = _authState.asStateFlow()

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    init {
        if (auth.currentUser != null) {
            _authState.value = AuthState.Authenticated
        }
    }

    fun updateFullName(value: String) = _uiState.update { it.copy(fullName = value) }
    fun updateUsername(value: String) = _uiState.update { it.copy(username = value) }
    fun updatePassword(value: String) = _uiState.update { it.copy(password = value) }
    fun updateConfirmPassword(value: String) = _uiState.update { it.copy(confirmPassword = value) }
    fun updateBirthdate(value: String) = _uiState.update { it.copy(birthdate = value) }
    fun updateLocation(value: String) = _uiState.update { it.copy(location = value) }

    private fun getFakeEmail(username: String): String = "$username@growreminder.com"

    fun signup() {
        val state = _uiState.value
        viewModelScope.launch {
            if (state.password != state.confirmPassword) {
                _authState.value = AuthState.Error("Mật khẩu xác nhận không trùng khớp")
                return@launch
            }

            val email = getFakeEmail(state.username)

            try {
                _authState.value = AuthState.Loading

                auth.createUserWithEmailAndPassword(email, state.password).await()

                auth.currentUser?.updateProfile(
                    UserProfileChangeRequest.Builder()
                        .setDisplayName(state.username)
                        .build()
                )?.await()

                val uid = auth.currentUser?.uid ?: return@launch
                val userData = mapOf(
                    "fullName" to state.fullName,
                    "username" to state.username,
                    "birthdate" to state.birthdate,
                    "location" to state.location,
                    "email" to email
                )
                db.collection("users").document(uid).set(userData).await()

                _authState.value = AuthState.Authenticated
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Đăng ký thất bại: ${e.message}")
            }
        }
    }

    fun loginWithUsernamePassword() {
        val state = _uiState.value
        val email = getFakeEmail(state.username)

        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                auth.signInWithEmailAndPassword(email, state.password).await()
                _authState.value = AuthState.Authenticated
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Sai mật khẩu hoặc username")
            }
        }
    }

    fun signOut() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    fun resetAuthState() {
        _authState.value = AuthState.Unauthenticated
    }

    fun signInWithGoogle(context: Context) {
        _authState.value = AuthState.Loading
        val credentialManager = CredentialManager.create(context)

        viewModelScope.launch {
            try {
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setServerClientId(context.getString(com.example.growreminder.R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .setAutoSelectEnabled(true)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                // ✅ CHUẨN: Gọi hàm suspend đúng
                val result = credentialManager.getCredential(context, request)

                // ✅ Đúng cách lấy credential
                val credential = result.credential
                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                    auth.signInWithCredential(firebaseCredential).await()

                    // ✅ Tạo username từ email
                    val user = auth.currentUser
                    val uid = user?.uid
                    val email = user?.email ?: ""
                    val suggestedUsername = email.substringBefore("@")

                    if (uid != null) {
                        val userDoc = Firebase.firestore.collection("users").document(uid).get().await()
                        if (!userDoc.exists()) {
                            val data = mapOf(
                                "fullName" to (user.displayName ?: ""),
                                "email" to email,
                                "username" to suggestedUsername
                            )
                            Firebase.firestore.collection("users").document(uid).set(data).await()
                        }
                    }

                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error("Unsupported credential type")
                }
            } catch (e: Exception) {
                val message = when (e) {
                    is NoCredentialException -> "Bạn chưa chọn tài khoản nào"
                    is GetCredentialException -> "Lỗi xác thực: ${e.message}"
                    else -> "Đăng nhập Google thất bại: ${e.message}"
                }
                Log.e("GOOGLE_LOGIN", message, e)
                _authState.value = AuthState.Error(message)
            }
        }
    }


}
