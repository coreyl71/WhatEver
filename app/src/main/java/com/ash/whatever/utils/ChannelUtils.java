package com.ash.whatever.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 将英文的channel转换为对应的汉字
 */
public class ChannelUtils {
    private static Map<String, String> map = new HashMap<>();
    public static String getChannelWord(String channel){
        initMap();
        return map.get(channel);
    }

    private static void initMap() {
        map.put(UrlUtils.WEIXIN,"微信" );
        map.put(UrlUtils.SHEHUI,"社会" );
        map.put(UrlUtils.GUONEI,"国内" );
        map.put(UrlUtils.GUOWAI,"国际" );
        map.put(UrlUtils.YULE,"娱乐" );
        map.put(UrlUtils.TIYU,"体育" );
        map.put(UrlUtils.KEJI, "科技");
        map.put(UrlUtils.JIANKANG,"健康" );
        map.put(UrlUtils.PINGGUO,"苹果" );
        map.put(UrlUtils.QIWEN,"奇闻" );
        map.put(UrlUtils.LVYOU,"旅游" );
    }
}
