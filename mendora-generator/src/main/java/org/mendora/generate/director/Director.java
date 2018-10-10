package org.mendora.generate.director;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.mendora.generate.jdbc.DbSourcesProvider;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/26
 * desc: 全局配置
 */
@Data
public class Director {
    /**
     * 数据库配置
     */
    private DbSourcesProvider.DbSources dbSources;
    /**
     * 表前缀
     */
    private String[] tablePrefixs;
    /**
     * 生成的目标路径,文件夹
     */
    private String targetPath;
    /**
     * pojo指导器
     */
    private PojoDirector pojoDirector;
    /**
     * repository指导器
     */
    private RepoDirector repoDirector;

    static Director newDirector(JSONObject director) {
        return director.toJavaObject(Director.class);
    }
}
