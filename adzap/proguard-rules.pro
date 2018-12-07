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
-dontwarn com.aiming.mdt.**.*
-dontwarn com.adt.**.*
-dontoptimize
-dontskipnonpubliclibraryclasses
-keepattributes *Annotation*
#adt
-keep class com.admuing.** { *; }
-keep class com.aiming.mdt.** { *; }
-keep class com.adt.** { *; }
#R
-keepclassmembers class **.R$* {
    public static <fields>;
}
-keep public class com.google.android.gms.** {
    public *;
}
-keep public class com.google.ads.** {
    public *;
}
-keep class com.facebook.ads.** { *; }
-keepattributes SourceFile,LineNumberTable
-keepattributes JavascriptInterface
-keep class android.webkit.JavascriptInterface {
   *;
}
-keep class com.unity3d.ads.** {
   *;
}
