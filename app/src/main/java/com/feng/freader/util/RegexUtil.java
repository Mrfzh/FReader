package com.feng.freader.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/9
 */
public class RegexUtil {
    /**
     * 获取 url 中的主域名，例如 www.xxx.com
     */
    public static String getMainDomain(String url) {
        Pattern p =Pattern.compile("www.(\\w+(\\.)?)+",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(url);
        matcher.find();
        return matcher.group();
    }
}
