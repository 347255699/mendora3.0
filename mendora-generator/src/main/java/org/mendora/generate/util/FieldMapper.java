package org.mendora.generate.util;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/26
 * showFullColumns: POJO字段映射器
 */
@FunctionalInterface
public interface FieldMapper {
    /**
     * 映射字段
     *
     * @param field 字段名称
     * @return 映射后的字段名称
     */
    String map(String field);
}
