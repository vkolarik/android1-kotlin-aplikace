@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.org.jetbrains.kotlin.kapt)
    alias(libs.plugins.com.google.dagger.hilt.android)
}

android {
    val versionMajor = 0
    val versionMinor = 0
    val versionPatch = 1
    val myVersionCode = versionMajor * 10000 + versionMinor * 100 + versionPatch
    val myVersionName = "${versionMajor}.${versionMinor}.${versionPatch}"


    namespace = "cz.mendelu.project"
    compileSdk = 34

    defaultConfig {
        applicationId = "pef.mendelu.project.xkolari1"
        minSdk = 26
        targetSdk = 34
        versionCode = myVersionCode
        versionName = myVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    //implementation(libs.material3)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.androidx.material3.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    implementation(libs.navigation.compose)

    implementation(libs.room.ktx)
    implementation(libs.room.viewmodel)
    implementation(libs.room.lifecycle)
    implementation(libs.room.runtime)
    kapt(libs.room.compiler.kapt)


    implementation(libs.hilt)
    implementation(libs.hilt.compose)
    kapt(libs.hilt.compiler.kapt)


    implementation(libs.moshi)
    kapt(libs.moshi.kapt)

    implementation(libs.lifecycle)

    implementation("androidx.core:core-splashscreen:1.0.0")

    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("io.coil-kt:coil-compose:2.6.0")

    implementation(libs.sentry)
}