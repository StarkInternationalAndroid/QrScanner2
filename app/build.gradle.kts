import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

val camerax_version = "1.3.0"
val accompanist_version = "0.32.0"

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.si.qrscanner2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.si.qrscanner2"
        minSdk = 30
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val gmailPass = System.getenv("GMAIL_ANDROID_PASS") ?: error("Env GMAIL_ANDROID_PASS not found")
        buildConfigField("String", "GMAIL_ANDROID_PASS", "\"${gmailPass}\"")
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
// Java
    java.toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        // Optional: vendor.set(JvmVendorSpec.[VENDOR])
    }

// Kotlin
    kotlinExtension.jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        // Optional: vendor.set(JvmVendorSpec.[VENDOR])
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            // picks the Angus Mail notice file
            pickFirsts += "META-INF/LICENSE.md"
            pickFirsts += "META-INF/NOTICE.md"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    // CameraX
    implementation("androidx.camera:camera-camera2:${camerax_version}")
    implementation("androidx.camera:camera-lifecycle:${camerax_version}")
    implementation ("androidx.camera:camera-view:1.4.0-alpha02")

    //
    implementation("com.google.accompanist:accompanist-permissions:$accompanist_version")

    // Zxing
    implementation ("com.google.zxing:core:3.5.2")

    // Angus mail
    implementation("org.eclipse.angus:jakarta.mail:2.0.0")
    implementation("org.eclipse.angus:angus-activation:2.0.0");
    implementation("jakarta.activation:jakarta.activation-api:2.1.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}