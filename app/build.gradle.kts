import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Properties
import java.util.TimeZone

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.ksp)
    id("kotlin-parcelize")
}

val buildProperties = Properties()
val buildPropertiesFile = rootProject.file("build.properties")
if (buildPropertiesFile.exists()) {
    buildProperties.load(FileInputStream(buildPropertiesFile))
}
val appVersionCode = buildProperties.getProperty("versionCode", "1").toInt()
val appVersionName: String? = buildProperties.getProperty("versionName", "1.0.0")
val appProductName: String? = buildProperties.getProperty("productName", "arkhe")
val now: Instant? = Clock.systemUTC().instant()
val localDateTime: LocalDateTime? = now?.atZone(TimeZone.getDefault().toZoneId())?.toLocalDateTime()
val timestampFormatter: DateTimeFormatter? = DateTimeFormatter.ofPattern("yyMMddHHmm")
val timestampString = localDateTime?.format(timestampFormatter)
val buildTimestamp = timestampString

android {
    namespace = "com.arkhe.languageswitcher"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.arkhe.languageswitcher"
        minSdk = 23
        targetSdk = 36
        versionCode = appVersionCode
        versionName = "$appVersionName.build$buildTimestamp"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        multiDexEnabled = true
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

base {
    archivesName.set("$appProductName-$appVersionName.build$buildTimestamp")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.bundles.lifecycle)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.bundles.koin)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.material.icons.extended)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}