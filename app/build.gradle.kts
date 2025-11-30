plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt")
}

android {
    namespace = "com.example.level_up_gamer"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.level_up_gamer"
        minSdk = 25
        targetSdk = 36
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
    testOptions {
        animationsDisabled = true
        unitTests {
            isIncludeAndroidResources = true

        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // --- IMPLEMENTATION (Código base) ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // ✅ USA SOLO UN BOM (El que viene de libs es mejor si está actualizado)
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    // implementation(libs.compose.material3) // Duplicado probable, comenta este

    implementation(libs.androidx.ui)
    implementation(libs.play.services.games)

    // Room (Dejaste manuales, está bien, pero asegurate que no estén en libs)
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // Navegación y Coil
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Iconos extendidos
    implementation("androidx.compose.material:material-icons-extended")

    // Accompanist (Ojo: Pager ya está en Compose Foundation en versiones nuevas, pero mantenlo si tu código lo usa)
    implementation("com.google.accompanist:accompanist-pager:0.28.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.28.0")


    // --- TESTING (Unit Tests - Corren en tu PC) ---
    testImplementation(libs.junit)
    testImplementation(kotlin("reflect"))
    testImplementation("io.mockk:mockk:1.13.8") // OJO: Usas MockK para unit tests
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    // Kotest (si lo usas)
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")


    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.5")

// ✅ CORRECTO: Mockito optimizado para Android (Dalvik/ART)
    androidTestImplementation("org.mockito:mockito-android:5.11.0")

// ✅ CORRECTO: Mockito Kotlin (sin el núcleo de PC para evitar conflictos)
    androidTestImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1") {
        exclude(group = "org.mockito", module = "mockito-core")
    }
    // Asegúrate de tener estas también para Compose
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
}
