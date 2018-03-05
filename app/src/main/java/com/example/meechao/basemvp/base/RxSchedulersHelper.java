package com.example.meechao.basemvp.base;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Publisher;

/**
 * Func：处理Rx线程
 * Desc:
 * Author：jihf
 * Data：2017-02-09 09:33
 * Mail：jihaifeng@meechao.com
 */
public class RxSchedulersHelper {
  public static <T> FlowableTransformer<T, T> io_main() {
    return upstream -> upstream
        // 指定 subscribe() 发生在 子 线程
        .subscribeOn(Schedulers.io())
        // 指定 unSubscribeOn() 发生在 子 线程
        .unsubscribeOn(Schedulers.io())
        // 指定 observeOn() 发生在 主 线程
        .observeOn(AndroidSchedulers.mainThread());
  }
}