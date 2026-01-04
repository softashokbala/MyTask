# General Settings
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
-dontwarn javax.annotation.**
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# Keep line numbers for crash reporting
-keepattributes SourceFile,LineNumberTable

# Kotlin
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.android.AndroidExceptionPreHandler {
    <init>();
}

# Hilt / Dagger
-keep class .**_Factory
-keep class .**_MembersInjector
-keep class dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper$LayoutInflaterFactory {
    <init>(...);
}

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Dao interface *
-dontwarn androidx.room.paging.**

# Jetpack Compose
-keep class androidx.compose.ui.tooling.preview.PreviewParameterProvider
-keep class androidx.compose.runtime.SnapshotStateKt { *; }

# Kotlin Serialization
-dontnote kotlinx.serialization.SerializationKt
-keep,allowobfuscation,allowoptimization class * {
    <init>(...);
}
-if @kotlinx.serialization.Serializable class *
-keepclassmembers class * {
    static **$$serializer serializer(...);
}
-keepnames @kotlinx.serialization.Serializable class *

# ViewModel
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# Keep project specific data classes (if any reflection is used)
-keep class com.abc.todo.data.model.** { *; }
