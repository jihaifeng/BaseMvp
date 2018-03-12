package com.example.meechao.basemvp;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2018-03-01 16:21
 * Mail：jihaifeng@meechao.com
 */
public class BaseApplication extends Application {
  private static BaseApplication instance;

  public synchronized static BaseApplication getInstance() {
    return instance;
  }

  @Override protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    //解决65535
    MultiDex.install(this);
  }
}
