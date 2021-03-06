package org.mendora.generate.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.mendora.generate.util.BeanUtils;
import org.mendora.generate.util.FieldMapper;
import org.mendora.generate.util.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/26
 * desc: jdbc驱动
 */
@Slf4j
public class JdbcDriver {
    private Connection conn;

    /**
     * 需要过滤的字符 java pojo field  -> sql field
     */
    private static final String SUFFIX_NULL = "Null";
    private static final String SUFFIX_VAL = "Val";

    /**
     * 驱动资源初始化
     */
    private JdbcDriver() {
    }

    /**
     * 链接测试
     *
     * @return 测试结果
     */
    public boolean connectTesting() {
        DbSourcesProvider.DbSources sources = JsonDbSourcesProvider.provider().provide();
        try {
            Class.forName(sources.getDriverClass());
            conn = DriverManager.getConnection(sources.getUrl(), sources.getUser(), sources.getPassword());
            if (conn != null && !conn.isClosed()) {
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    public static JdbcDriver newDriver() {
        return new JdbcDriver();
    }

    /**
     * 查询操作
     *
     * @param sql sql语句
     * @return 结果集
     * @throws Exception 查询异常
     */
    private ResultSet query(String sql) throws Exception {
        Statement stat = conn.createStatement();
        return stat.executeQuery(sql);
    }

    /**
     * 查询表字段信息
     *
     * @param tableName 表格名称
     * @return 表结构描述集合
     * @throws Exception
     */
    public List<TableDesc> showFullColumns(String tableName) throws Exception {
        ResultSet rs = query("show full columns from " + tableName);
        return parse(TableDesc.class, rs, field -> {
            if (field.endsWith(SUFFIX_VAL)) {
                field = field.substring(0, field.indexOf(SUFFIX_VAL));
            }
            if (field.endsWith(SUFFIX_NULL)) {
                field = field.substring(field.indexOf(SUFFIX_NULL));
            }
            return StringUtils.firstLetterToUpperCase(field);
        });
    }

    /**
     * 查询表名称集合
     *
     * @return 表名称集合
     * @throws Exception 查询异常
     */
    public List<String> showTables() throws Exception {
        final ResultSet rs = query("show tables");
        final List<String> tableNames = new ArrayList<>();
        while (rs.next()) {
            tableNames.add(rs.getString(1));
        }
        return tableNames;
    }

    /**
     * 解析结果集 ResultSet -> Pojo
     *
     * @param clazz  pojo对象
     * @param rs     结果集
     * @param mapper 字段映射器
     * @param <T>    pojo类型
     * @return
     * @throws Exception 解析异常
     */
    private <T> List<T> parse(Class<T> clazz, ResultSet rs, FieldMapper mapper) throws Exception {
        final List<T> list = new ArrayList<>();
        final BeanInfo bi = Introspector.getBeanInfo(clazz);
        final PropertyDescriptor[] pds = bi.getPropertyDescriptors();
        if (pds != null && pds.length > 0) {
            while (rs.next()) {
                final T t = clazz.newInstance();
                BeanUtils.filling(t, rs::getString, mapper);
                list.add(t);
            }
        }
        return list;
    }
}
