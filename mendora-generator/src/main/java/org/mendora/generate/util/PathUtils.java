package org.mendora.generate.util;


/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/26
 * desc:
 */
public class PathUtils {
    public static String root() {
        return PathUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    }
}
