package com.example.meechao.basemvp.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import java.util.Iterator;
import java.util.Stack;

/**
 * Func：
 * User：jihf
 * Data：2016-12-15
 * Time: 15:39
 * Mail：jihaifeng@meechao.com
 */
public class ActivityUtil {
  private static ActivityUtil instance;
  //Activity栈
  private Stack<Activity> activityStack = new Stack<>();

  public synchronized static ActivityUtil getInstance() {
    if (null == instance) {
      synchronized (ActivityUtil.class) {
        if (null == instance) {
          instance = new ActivityUtil();
        }
      }
    }
    return instance;
  }

  /**
   * 添加activity,入栈
   *
   * @param activity activity
   */
  public void addActivity(Activity activity) {
    if (null != activity) {
      activityStack.add(activity);
    }
  }

  /**
   * 移除activity，出栈
   *
   * @param activity activity
   */
  public void removeActivity(Activity activity) {
    if (null != activity && activityStack.contains(activity)) {
      activityStack.remove(activity);
    }
  }

  /**
   * 获取当前Activity（栈顶Activity）
   *
   * @return activity
   */
  public Activity getCurrentActivity() {
    return activityStack.lastElement();
  }

  /**
   * 结束当前activity,栈顶activity
   */
  public void finishCurrentActivity() {
    Activity activity = activityStack.lastElement();
    finishActivity(activity);
  }

  /**
   * 结束指定的Activity
   *
   * @param activity activity
   */
  public void finishActivity(Activity activity) {
    if (null != activity) {
      activityStack.remove(activity);
      activity.finish();
      activity = null;
    }
  }

  /**
   * 结束指定类名的Activity
   *
   * @param cls class
   */
  public void finishActivityByName(Class<?> cls) {
    for (Activity activity : activityStack) {
      if (activity.getClass().equals(cls)) {
        finishActivity(activity);
      }
    }
  }

  /**
   * 结束所有Activity
   */
  public void finishAllActivity() {
    for (Activity activity : activityStack) {
      finishActivity(activity);
    }
    activityStack.clear();
  }

  /**
   * 获取当前activity数量
   *
   * @return int
   */
  public int getActivityNum() {
    if (null != activityStack) {
      return activityStack.size();
    }
    return 0;
  }

  /**
   * 退出应用
   *
   * @param context 上下文
   */
  public void AppExit(Context context) {
    try {
      finishAllActivity();
      ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
      activityMgr.killBackgroundProcesses(context.getPackageName());
      System.exit(0);
      Process.killProcess(Process.myPid());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 无参数的跳转
   *
   * @param to 目标Activity
   */
  public void jumpTo(Class to) {
    jumpTo(to, null);
  }

  /**
   * 带参数的跳转
   *
   * @param to 目标Activity
   * @param bundle 参数
   */
  public void jumpTo(Class to, Bundle bundle) {
    Activity mCurrentActivity = getCurrentActivity();
    if (null != mCurrentActivity) {
      Intent intent = new Intent();
      intent.setClass(mCurrentActivity, to);
      if (null != bundle) {
        intent.putExtras(bundle);
      }
      mCurrentActivity.startActivity(intent);
    }
  }

  public void jumpToWithResultCode(Class to, Bundle bundle, int resultCode) {
    Activity mCurrentActivity = getCurrentActivity();
    if (null != mCurrentActivity) {
      Intent intent = new Intent();
      intent.setClass(mCurrentActivity, to);
      if (null != bundle) {
        intent.putExtras(bundle);
      }
      mCurrentActivity.startActivityForResult(intent, resultCode);
    }
  }

  /**
   * 移除当前activity以外的activity,不用foreach 避免 ConcurrentModificationException
   */
  public void removeOtherActivity() {
    Iterator<Activity> iterator = activityStack.iterator();
    while (iterator.hasNext()) {
      Activity activity = iterator.next();
      if (activity != getCurrentActivity()) {
        iterator.remove();   //注意这个地方
        activity.finish();
      }
    }
    //for (Activity activity : activityStack) {
    //  if (activity != getCurrentActivity()) {
    //    finishActivity(activity);
    //  }
    //}
  }
}
