package com.example.meechao.basemvp.base;

import android.util.Log;
import com.example.meechao.basemvp.App;
import com.example.meechao.basemvp.utils.NetWorkUtil;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.subscribers.ResourceSubscriber;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Func：封装Subscriber,预处理加载loading,网络请求等操作
 * Desc:实例化时需要传入 BaseView 或其子类的实例，
 * Author：JHF
 * Date：2018-03-01 16:09
 * Mail：jihaifeng@meechao.com
 */
public abstract class RxSubscriber<T> extends ResourceSubscriber<T> {
  private static final String TAG = RxSubscriber.class.getSimpleName().trim();

  private static final String SOCKETTIMEOUTEXCEPTION = "网络连接超时，请检查您的网络状态，稍后重试";
  private static final String CONNECTEXCEPTION = "网络连接异常，请检查您的网络状态";
  private static final String UNKNOWNHOSTEXCEPTION = "网络异常，请检查您的网络状态";

  private boolean showDialog;
  private BaseView mView;

  public RxSubscriber() {
    this.showDialog = false;
  }

  public RxSubscriber(BaseView view) {
    this(view, false);
  }

  public RxSubscriber(BaseView view, boolean showDialog) {
    if (null == view) {
      throw new NullPointerException("view is null.please check it first.");
    }
    this.mView = view;
    this.showDialog = showDialog;
  }

  @Override protected void onStart() {
    super.onStart();
    if (!NetWorkUtil.isNetworkConnected(App.getInstance().getApplicationContext())) {
      mView.showNetError();
      _onComplete();
      return;
    }
    if (showDialog) {
      mView.showLoading();
    }
    _onStart();
  }

  @Override public void onError(Throwable t) {
    if (showDialog) {
      mView.hideLoading();
    }
    if (t instanceof SocketTimeoutException) {
      Log.e(TAG, "onError: SocketTimeoutException----" + SOCKETTIMEOUTEXCEPTION);
      _onError(SOCKETTIMEOUTEXCEPTION);
    } else if (t instanceof ConnectException) {
      Log.e(TAG, "onError: ConnectException-----" + CONNECTEXCEPTION);
      _onError(CONNECTEXCEPTION);
    } else if (t instanceof UnknownHostException) {
      Log.e(TAG, "onError: UnknownHostException-----" + UNKNOWNHOSTEXCEPTION);
      _onError(UNKNOWNHOSTEXCEPTION);
    } else {
      _onError(t.getMessage());
    }
  }

  @Override public void onNext(T t) {
    if (showDialog) {
      mView.hideLoading();
    }
    _onNext(t);
  }

  @Override public void onComplete() {
    _onComplete();
  }

  /************自定义暴露出来的回调方法，默认实现_onNext,其余按需实现***********************/

  protected void _onStart() {
  }

  protected abstract void _onNext(T data);

  protected void _onError(String message) {
    if (null != mView) {
      mView.showRequestError(message);
    }
  }

  protected void _onComplete() {

  }
}
