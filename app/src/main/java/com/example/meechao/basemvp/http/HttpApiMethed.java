package com.example.meechao.basemvp.http;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2018-03-02 09:39
 * Mail：jihaifeng@meechao.com
 */
public class HttpApiMethed {
  private static final String TAG = HttpApiMethed.class.getSimpleName().trim();
  private static ApiService apiService;
  public static void init() {
    apiService = HttpHelper.getInstance().getApiService(ApiService.class);
  }
}
