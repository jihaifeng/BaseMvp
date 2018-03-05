package com.example.meechao.basemvp.base.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.example.meechao.basemvp.base.BasePresenter;
import com.example.meechao.basemvp.base.BaseView;
import com.example.meechao.basemvp.utils.LogUtil;

/**
 * Func：MVP架构baseActivity
 * Desc:
 * Author：JHF
 * Data：2017-02-07 09:08
 * Mail：jihaifeng@meechao.com
 */
public abstract class BaseMVPActivity<T extends BasePresenter> extends BaseSimpleActivity
    implements BaseView {
  protected T mPresenter;

  protected abstract T initPresenter();

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //presenter注入
    mPresenter = initPresenter();
    LogUtil.i(TAG, "--------onCreate--------");
  }

  @Override protected void onResume() {
    super.onResume();
    if (null != mPresenter) {
      mPresenter.attachView(this);
    }
    LogUtil.i(TAG, "-------onResume------" + this);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (null != mPresenter) {
      mPresenter.detachView();
    }
  }
}
