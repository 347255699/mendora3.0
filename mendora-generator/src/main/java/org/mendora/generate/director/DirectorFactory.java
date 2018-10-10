package org.mendora.generate.director;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author menfre
 * date: 2018/10/10
 * version: 1.0
 * desc:
 */
public class DirectorFactory {
    private static JSONObject director;

    private static class DirectorHolder {
        private static final Director DIRECTOR = Director.newDirector(director);
    }

    public static Director director() {
        return DirectorHolder.DIRECTOR;
    }

    /**
     * 提供初始数据
     *
     * @param bytes
     */
    public static void init(byte[] bytes) {
        director = JSON.parseObject(new String(bytes));
    }
}
