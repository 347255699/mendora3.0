package org.mendora.generate.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.mendora.generate.util.PathUtils;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/26
 * desc: 全局配置
 */
@Slf4j
public class Config {
    /**
     * 数据库配置
     */
    private static DbConfig DB_CONFIG;
    /**
     * 表名称数组
     */
    private static String[] TABLES;
    /**
     * 生成的目标路径,文件夹
     */
    private static String TARGET_PATH;
    /**
     * pojo包名
     */
    private static PojoConfig POJO_CONFIG;
    /**
     * 配置文件路径
     */
    private static final String CONFIG_FILE_PATH = "director.json";

    static {
        try {
            /**
             * 打开配置文件通道
             */
            RandomAccessFile aFile = new RandomAccessFile(PathUtils.root() + CONFIG_FILE_PATH, "r");
            final ByteBuffer buf = ByteBuffer.allocate(1024);
            final byte[] bytes = new byte[1024];
            StringBuilder jsonStr = new StringBuilder();
            int read = aFile.getChannel().read(buf);
            while (read != -1) {
                buf.flip();
                buf.get(bytes, 0, buf.limit());
                jsonStr.append(new String(bytes));
                buf.compact();
                read = aFile.getChannel().read(buf);
            }
            // Str -> JsonObject
            JSONObject config = JSON.parseObject(jsonStr.toString());

            // 解析配置信息
            JSONObject db = config.getJSONObject("db");
            DB_CONFIG = JSON.toJavaObject(db, DbConfig.class);
            JSONArray tables = config.getJSONArray("tables");
            TABLES = new String[tables.size()];
            tables.toArray(TABLES);
            TARGET_PATH = config.getString("targetPath");
            JSONObject pojo = config.getJSONObject("pojo");
            POJO_CONFIG = JSON.toJavaObject(pojo, PojoConfig.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static DbConfig dbConfig() {
        return DB_CONFIG;
    }

    public static String[] tables() {
        return TABLES;
    }

    public static String targetPath() {
        return TARGET_PATH;
    }

    public static PojoConfig pojoConfig() {
        return POJO_CONFIG;
    }
}
