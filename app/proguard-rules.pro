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

# Uncomment this to preserve the line Checkout information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line Checkout information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}

# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
#-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>


## dagger

# dagger2 https://github.com/google/dagger/issues/645
-dontwarn com.google.errorprone.annotations.*


# okhttp3
-dontwarn okio.**
-dontwarn javax.annotation.**


# retrofit2 http://square.github.io/retrofit/
-dontnote retrofit2.Platform
#-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions

# okhttp3 https://github.com/square/okhttp/issues/2230
-dontwarn okhttp3.**

# glide https://github.com/krschultz/android-proguard-snippets/blob/master/libraries/proguard-glide.pro
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class com.bumptech.glide.integration.okhttp3.OkHttpGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

-dontwarn okhttp3.internal.platform.ConscryptPlatform

-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault




##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
# Gson specific classes
#-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# For using GSON @Expose annotation

-keepattributes *Annotation*

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Application classes that will be serialized/deserialized over Gson
# -keep class mypersonalclass.data.model.** { *; }
# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

#-keep class com.shockwave.**
#-keep class com.google.android.gms.** { *; }
#
#-keep class com.huawei.hms.ads.** { *; }
#-keep interface com.huawei.hms.ads.** { *; }

-keepattributes Signature, InnerClasses, EnclosingMethod

#Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

#Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
 @retrofit2.http.* <methods>;
}

#Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

#Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

#Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

#Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

#With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
#and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

#Optional third party libraries. You can safely ignore those warnings.
-dontwarn com.squareup.okhttp.**
-dontwarn com.squareup.picasso.**
-dontwarn com.edmodo.cropper.**
-dontwarn org.slf4j.impl.**

#RxJava needs these two lines for proper operation.
-keep class rx.internal.util.unsafe.** { *; }
-keep class com.google.**
-dontwarn com.google.**
-dontnote com.google.**

-keep class okhttp3.**
-dontwarn okhttp3.**
-dontnote okhttp3.**

-keep class retrofit2.**
-dontwarn retrofit2.**
-dontnote retrofit2.**

-keepnames class rx.android.schedulers.AndroidSchedulers
-keepnames class rx.Observable
-keep class rx.schedulers.Schedulers {
 public static <methods>;
 public static ** test();
}
-keep class rx.schedulers.ImmediateScheduler {
  public <methods>;
}
-keep class rx.schedulers.TestScheduler {
public <methods>;
}

-keep class rx.subscriptions.Subscriptions {
 *;
}
-keep class rx.exceptions.** {
 public <methods>;
}
-keep class rx.subjects.** {
 public <methods>;
}

-keepclassmembers class android.webkit.** { *; }
-keep class android.webkit.** { *; }

#JavaScript rules and classes required to be kept
-keepclasseswithmembers class * {
  @android.webkit.JavascriptInterface <methods>;
}

-keepclasseswithmembernames class * {
  native <methods>;
}

-keepclassmembers class com.docusign.androidsdk.** { *; }
-keep class com.docusign.androidsdk.** { *; }

-dontwarn kotlinx.coroutines.**
-keep class kotlinx.coroutines.** { *; }
