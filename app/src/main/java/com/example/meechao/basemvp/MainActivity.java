package com.example.meechao.basemvp;

import android.widget.TextView;
import butterknife.BindView;
import com.example.meechao.basemvp.base.view.BaseSimpleActivity;

public class MainActivity extends BaseSimpleActivity {

  @BindView (R.id.tv) TextView tv;

  @Override protected int getLayoutId() {
    return R.layout.activity_main;
  }

  @Override protected void initViewAndEvent() {
    tv.setText("BaseSimpleActivity");
  }
}
