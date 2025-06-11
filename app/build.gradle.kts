plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.growreminder"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.growreminder"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // AndroidX & Compose
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.foundation.layout)
    implementation(libs.androidx.material.icons.extended)


    // Thêm dependency này cho lifecycle-runtime-compose
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")

    // ✅ Material 3 stable
    implementation(libs.material3)

    // ✅ Firebase BOM
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics.ktx)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.database.ktx)

    // ✅ Google Sign-In + Credentials
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.lifecycle.viewmodel.compose)
    // Chuyển sang sử dụng StateFlow thay vì LiveData
    // implementation(libs.androidx.compose.runtime.livedata)

    // ✅ Network (Retrofit, OkHttp, Gson)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp.logging)

    // ✅ Coil (image loading)
    implementation(libs.coil.compose)

    // ✅ Accompanist
    implementation(libs.accompanist.flowlayout)

    // ✅ Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("com.google.android.gms:play-services-auth:21.0.0")
    implementation("androidx.credentials:credentials:1.2.0-alpha03")
    implementation("androidx.credentials:credentials-play-services-auth:1.2.0-alpha03")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.0")
// Identity Credential API (Google Sign-In mới)
    implementation("androidx.credentials:credentials:1.2.1")
    implementation("androidx.credentials:credentials-play-services-auth:1.2.1")
    implementation("androidx.compose.material3:material3:1.2.1")

}