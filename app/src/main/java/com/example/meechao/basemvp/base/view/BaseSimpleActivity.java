package com.example.meechao.basemvp.base.view;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.meechao.basemvp.R;
import com.example.meechao.basemvp.utils.ActivityUtil;
import com.example.meechao.basemvp.utils.LogUtil;
import com.example.meechao.basemvp.utils.dialog.DialogUtil;
import com.example.meechao.basemvp.utils.dialog.PopupDialog;

/**
 * Func：正常的BaseActivity
 * Desc:
 * Author：jihf
 * Data：2017-03-09 09:32
 * Mail：jihaifeng@meechao.com
 */
public abstract class BaseSimpleActivity extends AppCompatActivity {
  public static String TAG = BaseSimpleActivity.class.getSimpleName().trim();
  /*顶部toolbar*/
  @BindView (R.id.toolbar) View toolbar;
  //***********************左边******************************
  /*整个左边的控件*/
  @BindView (R.id.ll_left) LinearLayout llLeft;
  /*左边图片按钮*/
  @BindView (R.id.ib_left) ImageButton ibLeft;
  /*左边文字按钮*/
  @BindView (R.id.tv_left) TextView tvLeft;
  //***********************右边******************************
  /*整个右边的控件*/
  @BindView (R.id.ll_right) LinearLayout llRight;
  /*右边图片按钮*/
  @BindView (R.id.ib_right) ImageButton ibRight;
  /*右边文字按钮*/
  @BindView (R.id.tv_right) TextView tvRight;
  //***********************标题******************************
  /*标题*/
  @BindView (R.id.tv_title) TextView tvTitle;
  /*整个头部根View*/
  @BindView (R.id.rl_toolbar_root) RelativeLayout rlToolbarRoot;
  /*分割线*/
  @BindView (R.id.view_line) View viewLine;
  /*主内容区域*/
  @BindView (R.id.content_frame) FrameLayout contentFrame;

  private Context mContext;

  /*抽象方法，子类实现*/
  protected abstract int getLayoutId();

  protected abstract void initViewAndEvent();

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //布局注入
    setContentView(getLayoutId());
    ButterKnife.bind(this);

    configToolbar();

    setActivityStatus(this);
    initViewAndEvent();
    mContext = this;
    LogUtil.i(TAG, "--------onCreate--------");
  }

  /**
   * 配置toolBar的默认状态
   */
  private void configToolbar() {
    tvLeft.setVisibility(View.GONE);
    tvRight.setVisibility(View.GONE);
    ibRight.setVisibility(View.GONE);
  }

  @Override protected void onStart() {
    super.onStart();
    LogUtil.i(TAG, "-------onStart------");
  }

  @Override protected void onPause() {
    LogUtil.i(TAG, "-------onPause------");
    super.onPause();
  }

  @Override protected void onResume() {
    super.onResume();
    LogUtil.i(TAG, "-------onResume------" + this);
  }

  @Override protected void onStop() {
    super.onStop();
    LogUtil.i(TAG, "-------onStop------");
  }

  @Override protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
  }

  @Override protected void onDestroy() {
    // 在Activity栈中移除Activity
    ActivityUtil.getInstance().removeActivity(this);

    LogUtil.i(TAG, "-------onDestroy------");
    super.onDestroy();
  }

  @Override protected void onRestart() {
    super.onRestart();
    LogUtil.i(TAG, "-------onRestart------");
  }

  @Override public void finish() {
    LogUtil.i(TAG, "-------finish------");
    ActivityUtil.getInstance().removeActivity(this);
    super.finish();
  }

  @Override public void setContentView(@LayoutRes int layoutResID) {
    try {
      View view = getLayoutInflater().inflate(R.layout.activity_base, null);
      FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.content_frame);
      // 将传入的layout加载到activity_base的content_frame里面
      getLayoutInflater().inflate(layoutResID, frameLayout, true);
      super.setContentView(view);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        if (canGoBack()) {
          finish();
        }
        break;
    }
    return true;
  }

  /**
   * Activity 统一配置
   *
   * @param activity 需要配置的Activity
   */
  private void setActivityStatus(Activity activity) {
    //设置Activity强制竖屏显示
    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    // 向Acitivty栈（数据类型为List）中添加Activity
    ActivityUtil.getInstance().addActivity(activity);
    //设置TAG
    TAG = ActivityUtil.getInstance().getCurrentActivity().getClass().getSimpleName().trim();
  }

  @Override public void onBackPressed() {
    if (canGoBack()) {
      super.onBackPressed();
    } else {
      exit();
    }
  }

  private void exit() {
    DialogUtil.alertPopDialog(this, "", "确定要退出吗?", new PopupDialog.ClickListenerInterface() {
      @Override protected void _doConfirm() {
        finish();
        ActivityUtil.getInstance().AppExit(getApplicationContext());
      }
    });
  }
  /*******************暴露给子类调用的方法**************************/

  /**
   * 是否有上一级的Activity可以返回
   *
   * @return true===可返回，false====不可返回
   */
  public boolean canGoBack() {
    return ActivityUtil.getInstance().getActivityNum() > 1;
  }

  /**
   * 控件查找
   *
   * @param id id
   * @param <E> e
   *
   * @return View
   */
  @SuppressWarnings ("unchecked") public final <E extends View> E getView(int id) {
    try {
      return (E) findViewById(id);
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

  /**
   * 设置titleBar左侧文字按钮
   *
   * @param text 文字内容
   * @param clickListener 点击事件
   */
  public void setTvLeft(String text, View.OnClickListener clickListener) {
    setTvLeft(text, -1, clickListener);
  }

  /**
   * 设置titleBar左侧文字按钮
   *
   * @param text 文字内容
   * @param textColor 文字颜色,-1表示不改变文字颜色
   * @param clickListener 点击事件
   */
  public void setTvLeft(String text, @ColorRes int textColor, View.OnClickListener clickListener) {
    if (!TextUtils.isEmpty(text)) {
      tvLeft.setVisibility(View.VISIBLE);
      if (textColor != -1) {
        tvLeft.setTextColor(getResources().getColor(textColor));
      }
      tvLeft.setText(text);
      if (null != clickListener) {
        tvLeft.setOnClickListener(clickListener);
      }
    } else {
      tvLeft.setVisibility(View.GONE);
    }
  }

  /**
   * 设置titleBar左侧文字按钮
   *
   * @param text 文字内容
   */
  public void setTvRight(String text) {
    tvRight.setText(text);
  }

  /**
   * 设置titleBar左侧文字按钮
   *
   * @param text 文字内容
   * @param clickListener 点击事件
   */
  public void setTvRight(String text, View.OnClickListener clickListener) {
    setTvRight(text, -1, clickListener);
  }

  /**
   * 设置titleBar右侧文字按钮
   *
   * @param text 文字内容
   * @param textColor 文字颜色,-1表示不改变文字颜色
   * @param clickListener 点击事件
   */
  public void setTvRight(String text, @ColorRes int textColor, View.OnClickListener clickListener) {
    if (!TextUtils.isEmpty(text)) {
      tvRight.setVisibility(View.VISIBLE);
      if (textColor != -1) {
        tvRight.setTextColor(getResources().getColor(textColor));
      }
      tvRight.setText(text);
      if (null != clickListener) {
        tvRight.setOnClickListener(clickListener);
      }
    } else {
      tvRight.setVisibility(View.GONE);
    }
  }

  /**
   * 设置右侧图片按钮
   *
   * @param drawableId 图片
   * @param clickListener 点击事件
   */
  public void setIbRight(@DrawableRes int drawableId, View.OnClickListener clickListener) {
    ibRight.setBackgroundResource(drawableId);
    ibRight.setVisibility(View.VISIBLE);
    if (null != clickListener) {
      ibRight.setOnClickListener(clickListener);
    }
  }

  /**
   * 隐藏ToolBar
   */
  public void hideToolbar() {
    toolbar.setVisibility(View.GONE);
  }

  /**
   * 隐藏ToolBar下面的分割线
   */
  public void hideViewLine() {
    viewLine.setVisibility(View.GONE);
  }
}
