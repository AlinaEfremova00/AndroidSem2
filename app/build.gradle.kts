plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.detekt)
}

android {
    namespace = "ru.itis.first"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.itis.first"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
    buildToolsVersion = "34.0.0"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.adapter.rxjava2)

    // okhttp
    implementation (libs.okhttp)
    implementation (libs.okhttp3.okhttp)
    implementation (libs.logging.interceptor)

    // coroutines
    implementation (libs.kotlinx.coroutines.android)

    // gson
    implementation (libs.gson)

    // gradle
    implementation (libs.glide)
}


detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config.setFrom("C:\\Users\\Alina\\AndroidStudioProjects\\sem2\\config\\detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
    baseline = file("/User/Alina/AndroidStudioProjects/sem2/config/baseline.xml") // a way of suppressing issues before introducing detekt
}