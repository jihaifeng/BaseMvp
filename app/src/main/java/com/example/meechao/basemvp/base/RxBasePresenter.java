package com.example.meechao.basemvp.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Func：基于Rx的Presenter封装,控制订阅的生命周期
 * Desc:
 * Author：JHF
 * Date：2018-03-01 14:35
 * Mail：jihaifeng@meechao.com
 */
public class RxBasePresenter<T extends BaseView> implements BasePresenter<T> {
  protected T mView;
  protected CompositeDisposable mCompositeDisposable;

  /**
   * 订阅事件
   *
   * @param disposable d
   */
  protected void addSubscribe(Disposable disposable) {
    if (mCompositeDisposable == null) {
      mCompositeDisposable = new CompositeDisposable();
    }
    mCompositeDisposable.add(disposable);
  }

  /**
   * 取消所有订阅
   */
  protected void unSubscribe() {
    if (mCompositeDisposable != null) {
      mCompositeDisposable.clear();
    }
  }

  /**
   * 绑定View
   *
   * @param view v
   */
  @Override public void attachView(T view) {
    this.mView = view;
  }

  /**
   * 解绑View
   */
  @Override public void detachView() {
    this.mView = null;
    unSubscribe();
  }
}
