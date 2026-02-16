import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

// Version management function
fun genVersion(): Pair<Int, String> {
    val versionPro = Properties()
    val versionFile = file("../versions.properties")
    if (versionFile.exists()) {
        versionPro.load(FileInputStream(versionFile))
    }
    val major = versionPro["major"]?.toString()?.trim()?.toIntOrNull() ?: 1
    val minor = versionPro["minor"]?.toString()?.trim()?.toIntOrNull() ?: 0
    val patch = versionPro["patch"]?.toString()?.trim()?.toIntOrNull() ?: 0
    val build = versionPro["build"]?.toString()?.trim()?.toIntOrNull() ?: 0
    val verCode = (major * 1_000_000) + (minor * 10_000) + (patch * 100) + build
    val verName = "$major.$minor.$patch"
    return Pair(verCode, verName)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.appcompat)

            // Coroutines Android
            implementation(libs.kotlinx.coroutines.android)

            // Koin Android
            implementation(libs.koin.android)
        }

        commonMain.dependencies {
            // Compose
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material)
            implementation(libs.compose.material3)
            implementation(libs.compose.icons.extended)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // Navigation
            implementation(libs.navigation.compose)

            // Koin Core & Compose
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // Coroutines
            implementation(libs.kotlinx.coroutines.core)

            // DateTime
            implementation(libs.kotlinx.datetime)

            // Serialization
            implementation(libs.kotlinx.serialization.json)

            // DataStore
            implementation(libs.datastore.preferences)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "deepankumarpn.github.io.expensetracker"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    val (verCode, verName) = genVersion()

    defaultConfig {
        applicationId = "deepankumarpn.github.io.expensetracker"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = verCode
        versionName = verName
    }

    // Signing configuration
    signingConfigs {
        create("release") {
            val localProperties = Properties().apply {
                val file = rootProject.file("local.properties")
                if (file.exists()) load(file.inputStream())
            }
            val storeFilePath = localProperties.getProperty("RELEASE_STORE_FILE")
            if (!storeFilePath.isNullOrEmpty()) {
                storeFile = file(storeFilePath)
            }
            storePassword = localProperties.getProperty("RELEASE_STORE_PASSWORD", "")
            keyAlias = localProperties.getProperty("RELEASE_KEY_ALIAS", "")
            keyPassword = localProperties.getProperty("RELEASE_KEY_PASSWORD", "")
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/DEPENDENCIES"
            excludes += "/META-INF/LICENSE"
            excludes += "/META-INF/LICENSE.txt"
            excludes += "/META-INF/NOTICE"
            excludes += "/META-INF/NOTICE.txt"
            excludes += "/META-INF/INDEX.LIST"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // APK naming
    applicationVariants.all {
        val (_, verName) = genVersion()
        val versionPro = Properties()
        val versionFile = file("../versions.properties")
        if (versionFile.exists()) {
            versionPro.load(FileInputStream(versionFile))
        }
        val buildNum = versionPro["build"]?.toString()?.trim() ?: "0"

        val appName = "expensetracker_${verName}_${buildType.name}_RC${buildNum}"
        outputs.all {
            val output = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
            output.outputFileName = "$appName.apk"
        }
    }
}

// AAB naming
tasks.whenTaskAdded {
    if (name.startsWith("bundle")) {
        val match = Regex("bundle(Debug|Release)").find(name) ?: return@whenTaskAdded
        val buildType = match.groupValues[1].lowercase()
        val (_, verName) = genVersion()
        val versionPro = Properties()
        val versionFile = file("../versions.properties")
        if (versionFile.exists()) {
            versionPro.load(FileInputStream(versionFile))
        }
        val buildNum = versionPro["build"]?.toString()?.trim() ?: "0"
        val appName = "expensetracker_${verName}_${buildType}_RC${buildNum}"

        doLast {
            val aabDir = layout.buildDirectory.dir("outputs/bundle/$buildType").get().asFile
            aabDir.listFiles()?.filter { it.extension == "aab" }?.forEach { file ->
                file.renameTo(File(aabDir, "$appName.aab"))
            }
        }
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)

    // Platform-specific Firebase & Google dependencies (Android only)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.installations)
    implementation(libs.play.services.ads.identifier)
    implementation(libs.google.api.client.android)
    implementation(libs.google.api.services.sheets)
    implementation(libs.google.oauth.client.jetty)
}
