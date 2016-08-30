package com.ash.whatever.utils;

/**
 * 获取数据接口网址
 * Created by Corey on 2016/6/21.
 */
public class UrlUtils {

    // 申请数据网址的根url
    public static final String BASE_URL = "http://api.huceo.com/";
    // 申请数据时使用的key
    public static final String KEY = "71ea928b647bafb5cb3518217d129f61";
    // 定义不同的数据频道channel为静态常量
    public static final String WEIXIN = "wxnew";
    public static final String SHEHUI = "social";
    public static final String GUONEI = "guonei";
    public static final String GUOWAI = "world";
    public static final String YULE = "huabian";
    public static final String TIYU = "tiyu";
    public static final String KEJI = "keji";
    public static final String JIANKANG = "health";
    public static final String PINGGUO = "apple";
    public static final String QIWEN = "qiwen";
    public static final String LVYOU = "travel";
    // 图灵机器人网址
    public static final String TULING_URL = "http://op.juhe.cn/robot/index";
    public static final String MESSAGE = "?key=66799ca4417cee6bbf3324babbf697c6&info=";

    // 申请数据时是否随机获取，1代表随机获取
    public static final int IS_RAND = 1;

    /**
     * 获取数据接口网址
     *
     * @param channel 新闻频道
     * @param num     每次访问数据获取的新闻条数
     * @return 网址
     */
    public static String getUrl(String channel, int num) {

        return getUrl(channel, 1, num, IS_RAND);
    }


    /**
     * 获取数据接口网址
     *
     * @param channel 新闻频道
     * @param page    申请访问第几页的新闻
     * @param num     每次访问数据获取的新闻条数
     * @param rand    是否随机获取新闻数据
     * @return 网址
     */
    public static String getUrl(String channel, int page, int num, int rand) {

        String url = BASE_URL + channel + "/?key=" + KEY + "&page=" + page + "&num=" + num + "&rand=" + rand;
        return url;
    }

    public static String getUrl(String channel, int page, int num, int rand, String word) {
        String url;
        if (word == null) {
            url = getUrl(channel, page, num, rand);
        } else {
            url = getUrl(channel, page, num, rand) + "&word=" + word;
        }
        return url;
    }

    // 推荐页面的网址拼接
    public static String getUrl(int num) {

        return getUrl(WEIXIN, 1, num, IS_RAND);
    }

    // 图灵机器人的网址拼接
    public static String getMessageUrl() {
        return TULING_URL + MESSAGE;
    }
}
