package com.wander.danmu.Utils;

import android.text.TextUtils;

/**
 * Created by wander on 2015/12/7.
 * email 805677461@qq.com
 */
public class StringUtil {

    /**
     * 用于区别假人和用户的头像取图，服务器中没有假人的小图
     *
     * @param url
     * @param size o(original原图)、s(120*120)、m(180*180)、b(300*300)
     * @param uid  判断是否为假人id
     * @return
     */
    public static String smartPicUrl(String url, char size, String uid) {
        uid = TextUtils.isEmpty(uid) ? "" : uid;
        if (!uid.startsWith("50000") && !TextUtils.isEmpty(url) && (url.endsWith("jpg") || url.endsWith("JPG"))) {
            int pointPos = url.lastIndexOf(".");
            if (pointPos > 0) {
                //后缀
                String suffix = url.substring(pointPos);
                //前缀
                String prefix = url.substring(0, pointPos);
                if (prefix.endsWith("_s") || prefix.endsWith("_m") || prefix.endsWith("_b")) {
                    if (size == 'o') {
                        prefix = prefix.substring(0, prefix.length() - 2);
                    } else {
                        prefix = prefix.substring(0, prefix.length() - 2);
                        prefix += "_" + size;
                    }
                } else {
                    if (size != 'o') {
                        prefix += "_" + size;
                    }
                }
                return prefix + suffix;
            }
        }
        return url;
    }
}
