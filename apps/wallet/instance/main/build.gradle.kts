@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
    id("androidx.baselineprofile")
}

val isCI = project.hasProperty("android.injected.signing.store.file")
var isAPK = gradle.startParameter.projectProperties["isApk"]?.toBoolean() ?: false

android {
    namespace = Build.namespacePrefix("TonKeeper")
    compileSdk = Build.compileSdkVersion

    defaultConfig {
        applicationId = "ton.notkeeper"
        minSdk = Build.minSdkVersion
        targetSdk = Build.compileSdkVersion
        versionCode = 600

        versionName = "5.4.44" // Format is "major.minor.patch" (e.g. "1.0.0") and only numbers are allowed

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    flavorDimensions += "version"

    productFlavors {
        create("default") {
            dimension = "version"
        }
        create("site") {
            dimension = "version"
            matchingFallbacks += listOf("default")
        }
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            if (isCI) {
                signingConfig = signingConfigs.getByName("release")
                manifestPlaceholders += if (isAPK) {
                    mapOf("build_type" to "site")
                } else {
                    mapOf("build_type" to "google_play")
                }
            } else {
                manifestPlaceholders += mapOf("build_type" to "manual")
            }
        }

        debug {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
            manifestPlaceholders += mapOf("build_type" to "internal_debug")
        }
    }

    experimentalProperties["android.experimental.art-profile-r8-rewriting"] = true
    experimentalProperties["android.experimental.r8.dex-startup-optimization"] = true

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }

    packaging {
        jniLibs {
            useLegacyPackaging = false
        }
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            excludes += setOf(
                "META-INF/INDEX.LIST",
                "META-INF/*.kotlin_module"
            )
        }
    }
}

androidComponents {
    beforeVariants(selector().withFlavor("version" to "site").withBuildType("debug")) {
        it.enable = false
    }
}

baselineProfile {
    saveInSrc = true
    dexLayoutOptimization = true
    mergeIntoMain = true
    baselineProfileRulesRewrite = true
}

dependencies {
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation(project(ProjectModules.Wallet.app))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidX.test)
    androidTestImplementation(libs.androidX.test.core)
    androidTestImplementation(libs.androidX.test.espresso)
    androidTestImplementation(libs.androidX.test.uiautomator)

    implementation(libs.androidX.profileinstaller)
    baselineProfile(project(":baselineprofile:main"))

}
