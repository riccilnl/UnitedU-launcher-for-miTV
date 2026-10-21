plugins {
    id("com.android.application") version "8.7.3"
}
android {
    namespace = "com.riccilnl.mitv.kids"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.riccilnl.mitv.kids"
        minSdk = 23
        targetSdk = 28
        versionCode = 1
        versionName = "0.1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
