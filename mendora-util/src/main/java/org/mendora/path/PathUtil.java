package org.mendora.path;

public class PathUtil {
    public static String root(){
        return PathUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    }
}
