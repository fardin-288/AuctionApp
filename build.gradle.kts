buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:8.0.2")
        classpath("com.google.gms:google-services:4.3.15")
        classpath ("io.realm:realm-gradle-plugin:10.11.1")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {

    id("com.android.application") version "8.0.2" apply false
    id ("com.android.library") version "8.0.2" apply false

}


apply(plugins: "realm-android" )

