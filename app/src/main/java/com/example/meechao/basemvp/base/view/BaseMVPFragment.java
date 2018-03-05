package com.example.meechao.basemvp.base.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.meechao.basemvp.base.BasePresenter;
import com.example.meechao.basemvp.base.BaseView;

/**
 * 带MVP和Dagger2的Activity基类
 * Created by xiarh on 2017/9/22.
 */

/**
 * Func：MVP架构baseFragment
 * Desc:
 * Author：JHF
 * Data：2017-02-07 09:08
 * Mail：jihaifeng@meechao.com
 */
public abstract class BaseMVPFragment<T extends BasePresenter> extends BaseSimpleFragment
    implements BaseView {

  protected boolean isVisible;
  private boolean isPrepared;

  protected T mPresenter;

  protected abstract T initPresenter();

  protected abstract void _lazyLoad();

  @Override public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    if (getUserVisibleHint()) {
      isVisible = true;
      lazyLoad();
    } else {
      isVisible = false;
    }
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mPresenter = initPresenter();
    if (null != mPresenter) {
      mPresenter.attachView(this);
    }
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    isPrepared = true;
    lazyLoad();
  }

  @Override public void onDestroy() {
    super.onDestroy();
    if (null != mPresenter) {
      mPresenter.detachView();
    }
  }

  private void lazyLoad() {
    if (!isPrepared || !isVisible) {
      return;
    }

    _lazyLoad();
  }
}
