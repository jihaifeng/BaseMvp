package com.example.meechao.basemvp.base;

import io.reactivex.functions.Function;

/**
 * Func：服务器异常处理
 * Desc:
 * Author：JHF
 * Date：2018-03-01 16:51
 * Mail：jihaifeng@meechao.com
 */
public abstract class RxPreFunc<T extends BaseResponse, R> implements Function<T, R> {
  @Override public R apply(T t) throws Exception {
    switch (t.code) {
      case -1:
        throw new RuntimeException("系统错误");
      case -2:
        throw new RuntimeException("未登录");
      case -3:
        throw new RuntimeException("登录状态丢失，请重新登录");
      default:
        return _call(t);
    }
  }

  protected abstract R _call(T response);
}
