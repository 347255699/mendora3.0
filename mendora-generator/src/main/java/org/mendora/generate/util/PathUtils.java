package org.mendora.generate.util;


/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/26
 * desc: 路径工具
 */
public class PathUtils {
    /**
     * 根路径
     * @return 根路径
     */
    public static String root() {
        return PathUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    }
}
