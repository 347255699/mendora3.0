package org.mendora.generate.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/26
 * desc:
 */
@Slf4j
@Data
public class DbConfig {
    private String user;
    private String password;
    private String url;
    private String driverClass;
}
