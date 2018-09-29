package org.mendora.generate.director;

import lombok.Data;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/26
 * desc: 数据库配置信息
 */
@Data
public class DbDirector {
    /**
     * 账户
     */
    private String user;
    /**
     * 密码
     */
    private String password;
    /**
     * 连接
     */
    private String url;
    /**
     * 驱动名称
     */
    private String driverClass;
}
