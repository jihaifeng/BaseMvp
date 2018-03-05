package com.example.meechao.basemvp.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.example.meechao.basemvp.R;

/**
 * Func：自定义dialog提示框,仿ios
 * Desc:
 * Author：JHF
 * Date：2018-03-01 16:14
 * Mail：jihaifeng@meechao.com
 */
public class PopupDialog extends Dialog {

  private Context context;//上下文
  private String title;//标题
  private String confirmButtonText;//确定按钮文字
  private String cancelButtonText;//取消按钮文字
  private String content;//内容文本
  private ClickListenerInterface clickListenerInterface;//确定 取消 按钮回调接口

  public abstract static class ClickListenerInterface {

    //确定按钮回调
    private void doConfirm(PopupDialog dialog) {
      dialog.dismiss();
      _doConfirm();
    }

    protected abstract void _doConfirm();

    //取消按钮回调
    private void doCancel(PopupDialog dialog) {
      dialog.dismiss();
      _doCancel();
    }

    void _doCancel() {
    }
  }

  /**
   * @param context 上下文
   * @param title 标题
   * @param contentText 内容
   * @param confirmButtonText 确认按钮文字
   * @param cancelButtonText 取消按钮文字
   */
  public PopupDialog(Context context, String title, String contentText, String confirmButtonText,
      String cancelButtonText) {
    super(context, R.style.dialog_style_ios);
    this.context = context;
    this.title = title;
    this.confirmButtonText = confirmButtonText;
    this.cancelButtonText = cancelButtonText;
    this.content = contentText;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    init();
  }

  /**
   * 初始化
   */
  public void init() {
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(R.layout.dialog_popup, null);
    setContentView(view);

    TextView tvTitle = view.findViewById(R.id.title);
    Button tvConfirm = view.findViewById(R.id.confirm);
    Button tvCancel = view.findViewById(R.id.cancel);
    TextView txt_content = view.findViewById(R.id.txt_content);

    txt_content.setText(content);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      tvConfirm.setBackground(
          context.getResources().getDrawable(R.drawable.dialog_bottom_right_btn));
    }
    if (confirmButtonText != null && !confirmButtonText.equals("")) {
      tvConfirm.setText(confirmButtonText);
    }
    if (title != null && !title.equals("")) {
      tvTitle.setText(title);
      tvTitle.setVisibility(View.VISIBLE);
    }
    if (cancelButtonText != null && !cancelButtonText.equals("")) {
      tvCancel.setText(cancelButtonText);
      tvCancel.setVisibility(View.VISIBLE);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        tvConfirm.setBackground(
            context.getResources().getDrawable(R.drawable.dialog_bottom_right_btn));
      }
    }

    tvConfirm.setOnClickListener(new clickListener());
    tvCancel.setOnClickListener(new clickListener());

    Window dialogWindow = getWindow();
    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
    DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
    lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
    dialogWindow.setAttributes(lp);
  }

  public void setClicklistener(ClickListenerInterface clickListenerInterface) {
    this.clickListenerInterface = clickListenerInterface;
  }

  private class clickListener implements View.OnClickListener {
    @Override public void onClick(View v) {
      int id = v.getId();
      if (id == R.id.confirm) {
        clickListenerInterface.doConfirm(PopupDialog.this);
      } else if (id == R.id.cancel) {
        clickListenerInterface.doCancel(PopupDialog.this);
      }
    }
  }
}
