package org.mendora.jdbc;

/**
 * 持久化基础接口
 *
 * @param <K> 主键类型
 * @param <T> 相关实体类型
 * @author menfre
 */
public interface BaseRepository<K, T> {

    /**
     * 提供相关联的表名称
     *
     * @return 关联表名称
     */
    String tableName();

    /**
     * 提供select语句构造器
     *
     * @return select构造器
     */
    default SelectBuilder select() {
        return new SelectBuilder(tableName());
    }
}
