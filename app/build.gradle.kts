import com.android.build.api.dsl.Lint

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.auctionapp"
    compileSdk = 33




    defaultConfig {
        applicationId = "com.example.auctionapp"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures{
        viewBinding = true;
    }
}

dependencies {
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.8.20")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.20")
    implementation ("androidx.fragment:fragment:1.6.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.firebase:firebase-core:21.1.1")
    implementation ("com.google.firebase:firebase-analytics:21.3.0")
    implementation ("com.google.firebase:firebase-auth:22.1.1")
    implementation ("com.google.firebase:firebase-database:20.2.2") // Use the appropriate version
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-storage-ktx:20.2.1")
    implementation("com.google.firebase:firebase-auth-ktx:22.1.1")
    implementation("com.android.support:support-annotations:28.0.0")
    implementation("com.google.firebase:firebase-storage:20.1.0")
    testImplementation("junit:junit:4.13.2")

    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation("com.squareup.picasso:picasso:2.3.3")
    implementation ("androidx.mediarouter:mediarouter:1.2.2")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
    implementation ("androidx.activity:activity-ktx:1.2.0")



    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")



}