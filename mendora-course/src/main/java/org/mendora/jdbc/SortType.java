package org.mendora.jdbc;

import java.util.Optional;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/11/30
 * desc: 排序类型
 */
public enum SortType {
    /**
     * 排序类型
     */
    ASC("ASC"),
    DESC("DESC");

    public final String val;

    SortType(String val) {
        this.val = val;
    }

    public static Optional<SortType> valOf(String val) {
        for (SortType keyWord : values()) {
            if (keyWord.val.equals(val)) {
                return Optional.of(keyWord);
            }
        }
        return Optional.empty();
    }
}
