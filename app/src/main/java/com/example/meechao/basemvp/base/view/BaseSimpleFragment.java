package com.example.meechao.basemvp.base.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.meechao.basemvp.App;
import com.example.meechao.basemvp.base.RxSubscriber;
import com.example.meechao.basemvp.http.HttpApiMethed;
import com.example.meechao.basemvp.utils.LogUtil;
import org.reactivestreams.Subscription;

/**
 * Func：正常的BaseFragment
 * Desc:
 * Author：JHF
 * Date：2018-03-02 11:20
 * Mail：jihaifeng@meechao.com
 */
public abstract class BaseSimpleFragment extends Fragment {
  public static String TAG = BaseSimpleFragment.class.getSimpleName().trim();
  public View view;

  private Fragment fragment;

  private Unbinder unbinder;

  protected abstract int getLayoutId();

  protected abstract void initViewAndEvent();

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    view = inflater.inflate(getLayoutId(), container, false);
    ViewGroup parent = (ViewGroup) view.getParent();
    if (null != parent) {
      parent.removeView(view);
    }

    unbinder = ButterKnife.bind(this, view);
    fragment = this;
    return view;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    TAG = getClass().getSimpleName().trim();
    LogUtil.i(TAG, "cur：" + getClass().getSimpleName().trim());
    initViewAndEvent();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    if (unbinder != null) {
      unbinder.unbind();
    }
  }

  @Override public void onDestroy() {
    super.onDestroy();
  }

  @Override public void onResume() {
    super.onResume();
  }

  @Override public void onPause() {
    super.onPause();
  }

  @SuppressWarnings ("unchecked") public final <E extends View> E getView(int id) {
    try {
      return (E) this.getActivity().findViewById(id);
    } catch (ClassCastException e) {
      LogUtil.i(TAG, "Can not cast view to concrete class: " + e);
      throw e;
    } catch (NullPointerException e) {
      LogUtil.i(TAG, "view is null: " + e);
      throw e;
    } catch (Exception e) {
      LogUtil.i(TAG, "unknownException : " + e);
      throw e;
    }
  }
}
