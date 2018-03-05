package com.example.meechao.basemvp.base;

/**
 * Func：presenter基类
 * Desc:
 * Author：JHF
 * Date：2018-03-01 14:25
 * Mail：jihaifeng@meechao.com
 */
public  interface BasePresenter<T extends BaseView> {

  void attachView(T view);

  void detachView();

}
