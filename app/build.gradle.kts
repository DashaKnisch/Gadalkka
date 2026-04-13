import org.gradle.api.tasks.javadoc.Javadoc

plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.dkkk.soothsayer"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dkkk.soothsayer"
        minSdk = 22
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

tasks.register<Javadoc>("generateJavadoc") {
    source = fileTree("src/main/java")
    classpath = files(android.bootClasspath) + files(android.applicationVariants.flatMap { it.javaCompileProvider.get().classpath.files })


    setDestinationDir(file("$buildDir/docs/javadoc"))

    options.encoding = "UTF-8"
}