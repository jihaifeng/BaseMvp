package com.example.meechao.basemvp.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.Toast;
import com.example.meechao.basemvp.App;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.io.File;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2017-12-06-0006 16:57
 * Mail：jihaifeng@meechao.com
 */
public class AppUtils {

  /**
   * 获取 App 版本号
   *
   * @return App 版本号 versionName
   */
  public static String getAppVersionName() {
    String packageName = App.getInstance().getPackageName();
    try {
      PackageManager pm = App.getInstance().getPackageManager();
      PackageInfo pi = pm.getPackageInfo(packageName, 0);
      return pi == null ? null : pi.versionName;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 获取 App 版本码
   *
   * @return App 版本码 versionCode
   */
  public static int getAppVersionCode() {
    String packageName = App.getInstance().getPackageName();
    try {
      PackageManager pm = App.getInstance().getPackageManager();
      PackageInfo pi = pm.getPackageInfo(packageName, 0);
      return pi == null ? -1 : pi.versionCode;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return -1;
    }
  }

  /**
   * 获取渠道名
   *
   * @return 如果没有获取成功，那么返回值为空
   */
  public static String getUmengChannelName() {
    return getAppMetaData("UMENG_CHANNEL");
  }

  /**
   * 获取application中指定的meta-data
   *
   * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
   */
  public static String getAppMetaData(String key) {
    if (TextUtils.isEmpty(key)) {
      return null;
    }
    String resultData = null;
    try {
      PackageManager packageManager = App.getInstance().getPackageManager();
      if (packageManager != null) {
        ApplicationInfo applicationInfo =
            packageManager.getApplicationInfo(App.getInstance().getPackageName(), PackageManager.GET_META_DATA);
        if (applicationInfo != null) {
          if (applicationInfo.metaData != null) {
            resultData = applicationInfo.metaData.getString(key);
          }
        }
      }
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return resultData;
  }

  /**
   * 安装软件
   *
   * @param file
   */
  private void installApk(Context context, File file) {
    Uri uri = Uri.fromFile(file);
    Intent install = new Intent(Intent.ACTION_VIEW);
    install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    install.setDataAndType(uri, "application/vnd.android.package-archive");
    // 执行意图进行安装
    context.startActivity(install);
  }

  /**
   * @param context
   * @param apkPath 要安装的APK
   * @param rootMode 是否是Root模式
   */
  public static void install(Context context, String apkPath, boolean rootMode) {
    if (rootMode) {
      installRoot(context, apkPath);
    } else {
      installNormal(context, apkPath);
    }
  }

  /**
   * 通过非Root模式安装
   *
   * @param context
   * @param apkPath
   */
  public static void install(Context context, String apkPath) {
    install(context, apkPath, false);
  }

  //普通安装
  private static void installNormal(Context context, String apkPath) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    // 由于没有在Activity环境下启动Activity,设置下面的标签
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    //版本在7.0以上是不能直接通过uri访问的
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      File file = (new File(apkPath));
      //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
      Uri apkUri = FileProvider.getUriForFile(context, "com.meechao.meehe.installApk", file);
      //添加这一句表示对目标应用临时授权该Uri所代表的文件
      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
      intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
    } else {
      intent.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");
    }
    context.startActivity(intent);
  }

  //通过Root方式安装
  private static void installRoot(Context context, String apkPath) {
    Observable.just(apkPath)
        .map(mApkPath -> "pm install -r " + mApkPath)
        .map(SystemManager:: RootCommand)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(integer -> {
          if (integer == 0) {
            Toast.makeText(context, "安装成功", Toast.LENGTH_SHORT).show();
          } else {
            Toast.makeText(context, "root权限获取失败,尝试普通安装", Toast.LENGTH_SHORT).show();
            install(context, apkPath);
          }
        });
  }
}
