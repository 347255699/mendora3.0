package org.mendora.generate.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.mendora.generate.config.Config;
import org.mendora.generate.config.DbConfig;
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
 * desc:
 */
@Slf4j
public class JdbcDriver {
    private DbConfig dbConfig;
    private Connection conn;

    private static final String SUFFIX_NULL = "Null";
    private static final String SUFFIX_VAL = "Val";

    private JdbcDriver() {
        dbConfig = Config.dbConfig();
        try {
            Class.forName(dbConfig.getDriverClass());
            conn = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUser(), dbConfig.getPassword());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static JdbcDriver newDriver() {
        return new JdbcDriver();
    }

    private ResultSet query(String sql) throws Exception {
        Statement stat = conn.createStatement();
        return stat.executeQuery(sql);
    }

    public List<TableDesc> desc(String tableName) throws Exception {
        ResultSet rs = query("desc " + tableName);
        List<TableDesc> tds = parse(TableDesc.class, rs, field -> {
            if (field.endsWith(SUFFIX_VAL)) {
                field = field.substring(0, field.indexOf(SUFFIX_VAL));
            }
            if (field.endsWith(SUFFIX_NULL)) {
                field = field.substring(field.indexOf(SUFFIX_NULL));
            }
            return StringUtils.firstLetterToUpperCase(field);
        });
        return tds;
    }

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
