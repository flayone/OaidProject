
#获取oaid所需相关混淆设置
    -keep, includedescriptorclasses class com.asus.msa.SupplementaryDID.** { *; }
    -keepclasseswithmembernames class com.asus.msa.SupplementaryDID.** { *; }
    -keep, includedescriptorclasses class com.asus.msa.sdid.** { *; }
    -keepclasseswithmembernames class com.asus.msa.sdid.** { *; }
    -keep public class com.netease.nis.sdkwrapper.Utils {public <methods>;}
    -keep class com.bun.miitmdid.**{*;}
    -keep class com.bun.lib.**{*;}
    -keep class com.samsung.android.deviceidservice.**{*;}
    -keep class a.**{*;}
    -dontwarn XI.**
    -keep class  XI.** {*;}