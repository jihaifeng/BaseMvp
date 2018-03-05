package com.example.meechao.basemvp.base;

/**
 * Func：view
 * Desc:
 * Author：JHF
 * Date：2018-03-01 14:30
 * Mail：jihaifeng@meechao.com
 */
public interface BaseView {
  /**
   * 显示加载动画
   */
  void showLoading();

  /**
   * 隐藏加载
   */
  void hideLoading();

  /**
   * 显示网络错误
   */
  void showNetError();

  /**
   * 显示请求异常
   */
  void showRequestError(String msg);

  /**
   * 显示提示
   */
  void showToast(String msg);
}
