package com.example.meechao.basemvp.http;

import android.util.Log;
import com.example.meechao.basemvp.App;
import com.example.meechao.basemvp.BuildConfig;
import com.example.meechao.basemvp.utils.NetWorkUtil;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Func：
 * Desc:
 * Author：jihf
 * Data：2017-02-09 09:33
 * Mail：jihaifeng@meechao.com
 */
public class HttpHelper {
  public static final String TAG = HttpHelper.class.getSimpleName().trim();
  private OkHttpClient client;
  private static HttpHelper instance;

  private HttpHelper() {
    initOkHttp();
  }

  public static HttpHelper getInstance() {
    if (null == instance) {
      synchronized (HttpHelper.class) {
        if (null == instance) {
          instance = new HttpHelper();
        }
      }
    }
    return instance;
  }

  /**
   * 自定义 okHttpClient
   */
  private void initOkHttp() {
    // 创建OkHttpClient.Builder对象builder
    OkHttpClient.Builder builder = new OkHttpClient.Builder();

    // 设置缓存机制
    File file = new File(App.getInstance().getExternalCacheDir(), "app_cache");
    Cache cache = new Cache(file, 1024 * 1024 * 50);//设置缓存文件最大值50M
    Interceptor cacheInterceptor = getCacheInterceptor(cache);
    builder.cache(cache);
    builder.addInterceptor(cacheInterceptor);
    builder.addNetworkInterceptor(cacheInterceptor);

    // 设置头部
    //Interceptor headerInterceptor = new Interceptor() {
    //  @Override public Response intercept(Chain chain) throws IOException {
    //    Request originalRequest = chain.request();
    //    Request.Builder requestBuilder = originalRequest.newBuilder()
    //        .header("AppType", "TPOS")
    //        .header("Content-Type", "application/json")
    //        .header("Accept", "application/json")
    //        .method(originalRequest.method(), originalRequest.body());
    //    Request request = requestBuilder.build();
    //    return chain.proceed(request);
    //  }
    //};
    //builder.addInterceptor(headerInterceptor);

    //设置请求公共参数
    //Interceptor publicParameterInterceptor = new Interceptor() {
    //  @Override
    //  public Response intercept(Chain chain) throws IOException {
    //    Request originalRequest = chain.request();
    //    Request request;
    //    String method = originalRequest.method();
    //    Headers headers = originalRequest.headers();
    //    HttpUrl modifiedUrl = originalRequest.url().newBuilder()
    //        // 公共参数
    //        .addQueryParameter("platform", "android")
    //        .addQueryParameter("version", "1.0.0")
    //        .build();
    //    request = originalRequest.newBuilder().url(modifiedUrl).build();
    //    return chain.proceed(request);
    //  }
    //};
    //builder.addInterceptor(publicParameterInterceptor);

    // 设置cookie
    CookieManager cookieManager = new CookieManager();
    cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
    //  new JavaNetCookieJar() 需要依赖   compile 'com.squareup.okhttp3:okhttp-urlconnection:3.2.0'
    builder.cookieJar(new JavaNetCookieJar(cookieManager));

    //设置log信息拦截器
    if (BuildConfig.DEBUG) {
      HttpLoggingInterceptor logInterceptor =
          new HttpLoggingInterceptor(message -> Log.i(TAG, "retrofit: " + message));
      logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
      builder.addInterceptor(logInterceptor);
    }

    builder.addInterceptor(chain -> {

      if (NetWorkUtil.isNetworkConnected(App.getInstance())) {
        return chain.proceed(chain.request());
      } else {
        throw new ConnectException("网络中断，请检查网络后重试");
      }
    });

    //设置超时
    builder.connectTimeout(10, TimeUnit.SECONDS);
    builder.readTimeout(200, TimeUnit.SECONDS);
    builder.writeTimeout(200, TimeUnit.SECONDS);

    //错误重连
    builder.retryOnConnectionFailure(true);

    //以上设置结束，才能build(),不然设置白搭
    client = builder.build();
  }

  public <T> T getApiService(String baseUrl, Class<T> cls) {
    Retrofit retrofit = createRetrofit(baseUrl, cls);
    return retrofit.create(cls);
  }

  public <T> T getApiService(Class<T> cls) {
    Retrofit retrofit = createRetrofit(UrlConstants.getBaseUrl(), cls);
    return retrofit.create(cls);
  }

  private <T> Retrofit createRetrofit(String baseUrl, Class<T> cls) {
    return new Retrofit.Builder()
        // baseUrl
        .baseUrl(baseUrl)
        // 自定义 okHttpClient
        .client(client)
        // json 解析器
        .addConverterFactory(GsonConverterFactory.create())
        // RxJava 适配器
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
  }

  /**
   * 获取缓存器
   *
   * @param cache 缓存
   *
   * @return 缓存器
   */
  private Interceptor getCacheInterceptor(Cache cache) {
    Interceptor cacheInterceptor = new Interceptor() {
      @Override public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetWorkUtil.isNetworkConnected(App.getInstance())) {
          request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
        }
        Response response = chain.proceed(request);
        if (NetWorkUtil.isNetworkConnected(App.getInstance())) {
          // 有网络时 设置缓存超时时间0个小时
          int maxAge = 0;
          response.newBuilder()
              .addHeader("Cache-Control", "public, max-age=" + maxAge)
              .removeHeader("Pragma")
              .build();
        } else {
          // 无网络时，设置超时为4周
          int maxStale = 60 * 60 * 24 * 28;
          response.newBuilder()
              .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
              .removeHeader("Pragma")
              .build();
        }
        return response;
      }
    };
    return cacheInterceptor;
  }
}
