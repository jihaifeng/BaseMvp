package com.example.meechao.basemvp.utils;

import com.example.meechao.basemvp.App;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Func：读取本地assets文件夹内的json文件数据
 * Desc:
 * Author：JHF
 * Date：2017-12-26 18:13
 * Mail：jihaifeng@meechao.com
 */
public class AssetUtils {
  private static final String TAG = AssetUtils.class.getSimpleName().trim();

  public static String getAssetsData(String assetsFileName) {
    InputStream is = null;
    ByteArrayOutputStream bos = null;
    try {
      is = App.getInstance().getApplicationContext().getAssets().open(assetsFileName);
      bos = new ByteArrayOutputStream();
      byte[] bytes = new byte[4 * 1024];
      int len;
      while ((len = is.read(bytes)) != -1) {
        bos.write(bytes, 0, len);
      }
      return new String(bos.toByteArray());
    } catch (Exception e) {
      e.printStackTrace();
      LogUtil.i(TAG, "getAssetsData Exception: " + e);
    } finally {
      try {
        if (is != null) {
          is.close();
        }
        if (bos != null) {
          bos.close();
        }
      } catch (IOException e) {
        LogUtil.i(TAG, "getAssetsData IOException: " + e);
      }
    }
    return null;
  }
}