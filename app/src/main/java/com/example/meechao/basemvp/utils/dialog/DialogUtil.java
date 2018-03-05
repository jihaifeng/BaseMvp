package com.example.meechao.basemvp.utils.dialog;

import android.content.Context;

/**
 * Func：dialog工具类
 * Desc:
 * Author：JHF
 * Date：2018-03-02 10:44
 * Mail：jihaifeng@meechao.com
 */
public class DialogUtil {
  /**
   * 仿IOS的提示框样式
   *
   * @param context 上下文
   * @param title 标题
   * @param message 提示内容
   * @param clickListener 点击事件
   */
  public static void alertPopDialog(Context context, String title, String message,
      PopupDialog.ClickListenerInterface clickListener) {
    alertPopDialog(context, title, message, "确定", "取消", clickListener);
  }

  /**
   * 仿IOS的提示框样式
   *
   * @param context 上下文
   * @param title 标题
   * @param message 提示内容
   * @param confirmBtnText 确认按钮的文字
   * @param clickListener 点击事件
   */
  public static void alertPopDialog(Context context, String title, String message,
      String confirmBtnText, PopupDialog.ClickListenerInterface clickListener) {
    alertPopDialog(context, title, message, confirmBtnText, "", clickListener);
  }

  /**
   * 仿IOS的提示框样式
   *
   * @param context 上下文
   * @param title 标题
   * @param message 提示内容
   * @param confirmBtnText 确认按钮的文字
   * @param cancelButtonText 取消按钮的文字
   * @param clickListener 点击事件
   */
  public static void alertPopDialog(final Context context, String title, String message,
      String confirmBtnText, String cancelButtonText,
      PopupDialog.ClickListenerInterface clickListener) {
    final PopupDialog popDialog =
        new PopupDialog(context, title, message, confirmBtnText, cancelButtonText);
    popDialog.setClicklistener(clickListener);
    popDialog.setCancelable(false);
    popDialog.setCanceledOnTouchOutside(false);
    popDialog.show();
  }
}
