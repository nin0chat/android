plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.nin0.chat"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.nin0.chat"
        minSdk = 21
        targetSdk = 35
        versionCode = 1000
        versionName = "1.0.0"

        buildConfigField("String", "API_URL", "\"https://chatapi.nin0.dev/api\"")
        buildConfigField("String", "SOCKET_URL", "\"wss://chatws.nin0.dev\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        named("debug") {
            applicationIdSuffix = ".debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(platform(libs.koin.bom))

    implementation(libs.bundles.androidx)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.kotlinx)
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.voyager)

    implementation(libs.syntakts)

}