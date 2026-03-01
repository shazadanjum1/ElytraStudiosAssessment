# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# General Android/Compose
-keep class androidx.compose.** { *; }
-keep class kotlin.** { *; }
-keep class kotlinx.coroutines.** { *; }

# Keep Material icons
-keep class androidx.compose.material.icons.** { *; }

# Retrofit / OkHttp
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# Keep OkHttp classes
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }

# Gson-
-keep class com.elytrastudios.assessment.data.model.** { *; }
-keepclassmembers class com.elytrastudios.assessment.data.model.** {
    <fields>;
}

# Hilt / Dagger
-keep class dagger.hilt.** { *; }
-keep class *Injector { *; }
-keep class *Hilt_* { *; }
-keep class *Dagger* { *; }

# Room
-keep class androidx.room.** { *; }
-keep class com.elytrastudios.assessment.data.local.** { *; }
-keepclassmembers class com.elytrastudios.assessment.data.local.** {
    <fields>;
}

# Timber
-dontwarn timber.log.**

# Shimmer
-dontwarn com.valentinilk.shimmer.**




# Ignore other Java compiler APIs if flagged
-dontwarn javax.annotation.**
-dontwarn java.compiler.**

-keep class com.google.errorprone.annotations.** { *; }

# Ignore compile-time only classes
-dontwarn javax.lang.model.**
-dontwarn com.google.errorprone.annotations.**
-dontwarn javax.lang.model.element.**

# Keep errorprone annotations (avoid stripping issue)
#-keep class com.google.errorprone.annotations.** { *; }

# Ignore JSR 305 (sometimes also required)
-dontwarn javax.annotation.**



# Remove them safely
-assumenosideeffects class com.google.errorprone.annotations.** { *; }

-keep,allowobfuscation @interface com.google.errorprone.annotations.**

