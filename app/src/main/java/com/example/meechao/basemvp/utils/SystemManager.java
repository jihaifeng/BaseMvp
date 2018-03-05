package com.example.meechao.basemvp.utils;

import android.app.ActivityManager;
import android.content.Context;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2017-12-07-0007 9:12
 * Mail：jihaifeng@meechao.com
 */
public class SystemManager {
  /**
   * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
   *
   * @param command 命令：String apkRoot="chmod 777 "+getPackageCodePath();
   *
   * @return 0 命令执行成功
   */
  public static int RootCommand(String command) {
    Process process = null;
    DataOutputStream os = null;
    try {
      process = Runtime.getRuntime().exec("su");
      os = new DataOutputStream(process.getOutputStream());
      os.writeBytes(command + "\n");
      os.writeBytes("exit\n");
      os.flush();
      int i = process.waitFor();

      LogUtil.i("SystemManager", "i:" + i);
      return i;
    } catch (Exception e) {
      LogUtil.i("SystemManager", e.getMessage());
      return -1;
    } finally {
      try {
        if (os != null) {
          os.close();
        }
        process.destroy();
      } catch (Exception e) {
      }
    }
  }

  /**
   * 提升读写权限
   *
   * @param filePath 文件路径
   *
   * @return
   *
   * @throws IOException
   */
  public static void setPermission(String filePath) {
    String command = "chmod " + "777" + " " + filePath;
    Runtime runtime = Runtime.getRuntime();
    try {
      runtime.exec(command);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private final static int kSystemRootStateUnknow = -1;
  private final static int kSystemRootStateDisable = 0;
  private final static int kSystemRootStateEnable = 1;
  private static int systemRootState = kSystemRootStateUnknow;

  public static boolean isRootSystem() {
    if (systemRootState == kSystemRootStateEnable) {
      return true;
    } else if (systemRootState == kSystemRootStateDisable) {
      return false;
    }
    File f = null;
    final String kSuSearchPaths[] = {
        "/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/"
    };
    try {
      for (int i = 0; i < kSuSearchPaths.length; i++) {
        f = new File(kSuSearchPaths[i] + "su");
        if (f != null && f.exists()) {
          systemRootState = kSystemRootStateEnable;
          return true;
        }
      }
    } catch (Exception e) {
    }
    systemRootState = kSystemRootStateDisable;
    return false;
  }

  /**
   * 判断应用是否已经启动
   *
   * @param context 一个context
   * @param packageName 要判断应用的包名
   *
   * @return boolean
   */
  public static boolean isAppAlive(Context context, String packageName) {
    ActivityManager activityManager =
        (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    List<ActivityManager.RunningAppProcessInfo> processInfos =
        activityManager.getRunningAppProcesses();
    for (int i = 0; i < processInfos.size(); i++) {
      if (processInfos.get(i).processName.equals(packageName)) {
        LogUtil.i("NotificationLaunch",
            String.format("the %s is running, isAppAlive return true", packageName));
        return true;
      }
    }
    LogUtil.i("NotificationLaunch",
        String.format("the %s is not running, isAppAlive return false", packageName));
    return false;
  }
}
