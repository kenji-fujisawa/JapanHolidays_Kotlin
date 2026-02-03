plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    `maven-publish`
}

android {
    namespace = "jp.uhimania.japanholidays"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // App Startup
    implementation(libs.androidx.startup.runtime)

    // Ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    testImplementation(libs.ktor.client.mock)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

afterEvaluate {
    publishing {
        publications {
            val libraryVersion: String by project
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "jp.uhimania.japanholidays"
                artifactId = "japanholidays"
                version = libraryVersion
            }
        }

        repositories {
            maven {
                url = uri(layout.settingsDirectory.dir("repository"))
                name = "repository"
            }
        }
    }
}
