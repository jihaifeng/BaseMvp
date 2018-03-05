package com.example.meechao.basemvp.http;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2017-11-01-0001 11:53
 * Mail：jihaifeng@meechao.com
 */
public class UrlConstants {


  private static String MEECHAO_TEST_URL = "http://apps.meechao.com:8088/";
  // 合集版域名： http://114.215.180.143:8080
  // 正式域名：https://app.meechao.com/
  private static String MEECHAO_OFFICIAL_URL = "https://app.meechao.com/";
  public static final String SHARE_ARTICLE = "http://activity.meechao.com:8080/Article/ArticleShare.html?articleId=";
  public static final String SHARE_COLLECTION =
      "http://activity.meechao.com:8080/Collection/CollectionShare.html?collectionId=";

  private static String BASE_URL = MEECHAO_TEST_URL;

  public static void init(boolean isTestServer) {
    // 测试
    BASE_URL = isTestServer ? MEECHAO_TEST_URL : MEECHAO_OFFICIAL_URL;
  }

  public static String getBaseUrl() {
    return BASE_URL;
  }
}
