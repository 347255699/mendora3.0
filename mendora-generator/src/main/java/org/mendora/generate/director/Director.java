package org.mendora.generate.director;

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
public class Director {
    /**
     * 数据库配置
     */
    private static DbDirector DB_DIRECTOR;
    /**
     * 表名称数组
     */
    private static String[] TABLES;
    /**
     * 生成的目标路径,文件夹
     */
    private static String TARGET_PATH;
    /**
     * pojo指导器
     */
    private static PojoDirector POJO_DIRECTOR;
    /**
     * repository指导器
     */
    private static RepoDirector REPO_DIRECTOR;
    /**
     * 配置文件路径
     */
    private static final String DIRECTOR_FILE_PATH = "director.json";

    static {
        try {
            /**
             * 打开配置文件通道
             */
            RandomAccessFile aFile = new RandomAccessFile(PathUtils.root() + DIRECTOR_FILE_PATH, "r");
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
            JSONObject director = JSON.parseObject(jsonStr.toString());

            // 解析指导信息
            JSONObject db = director.getJSONObject("db");
            DB_DIRECTOR = JSON.toJavaObject(db, DbDirector.class);
            JSONArray tables = director.getJSONArray("tables");
            TABLES = new String[tables.size()];
            tables.toArray(TABLES);
            TARGET_PATH = director.getString("targetPath");
            POJO_DIRECTOR = JSON.toJavaObject(director.getJSONObject("pojo"), PojoDirector.class);
            REPO_DIRECTOR = JSON.toJavaObject(director.getJSONObject("repo"), RepoDirector.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static DbDirector dbDirector() {
        return DB_DIRECTOR;
    }

    public static String[] tables() {
        return TABLES;
    }

    public static String targetPath() {
        return TARGET_PATH;
    }

    public static PojoDirector pojoDirector() {
        return POJO_DIRECTOR;
    }

    public static RepoDirector repoDirector() {
        return REPO_DIRECTOR;
    }
}
