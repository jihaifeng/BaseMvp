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
public class App extends Application {
  private static App instance;

  public synchronized static App getInstance() {
    return instance;
  }

  @Override protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }
}
