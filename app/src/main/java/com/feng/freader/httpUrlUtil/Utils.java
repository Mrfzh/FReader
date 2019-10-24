package com.feng.freader.httpUrlUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Feng Zhaohao
 * Created on 2019/10/24
 */
class Utils {

    /**
     * 读取并返回 InputStream 的内容
     */
    static String readInputStream(InputStream is) throws IOException {
        BufferedReader br = null;
        StringBuilder res = new StringBuilder();
        try {
            // 对输入流进行读取
            br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                res.append(line);
            }
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return res.toString();
    }
}
