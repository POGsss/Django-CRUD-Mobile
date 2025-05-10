plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.djangocrud"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.djangocrud"
        minSdk = 24
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)

    implementation(libs.retrofit)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.okhttp)

    implementation(libs.glide)
    implementation(libs.cardview)
    implementation(libs.recyclerview)

    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}