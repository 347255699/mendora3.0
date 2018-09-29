package org.mendora.generate.util;

import lombok.extern.slf4j.Slf4j;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/26
 * showFullColumns:
 */
@Slf4j
public class BeanUtils {
    private static final String FIELD_CLASS = "class";

    /**
     * 实例化并填充数据到pojo
     *
     * @param clazz
     * @param vf
     * @param <T>
     * @throws Exception
     */
    public static <T> void filling(Class<T> clazz, ValueFactory vf) throws Exception {
        filling(clazz.newInstance(), vf, null);
    }

    /**
     * 填充数据到pojo实例
     *
     * @param t
     * @param vf
     * @param <T>
     * @throws Exception
     */
    public static <T> void filling(T t, ValueFactory vf) throws Exception {
        filling(t, vf, null);
    }

    /**
     * 填充数据到pojo实例并提供字段映射器
     *
     * @param t
     * @param vf
     * @param <T>
     * @throws Exception
     */
    public static <T> T filling(T t, ValueFactory vf, FieldMapper fm) throws Exception {
        final BeanInfo bi = Introspector.getBeanInfo(t.getClass());
        final PropertyDescriptor[] pds = bi.getPropertyDescriptors();
        Arrays.asList(pds).forEach(pd -> {
            if (!FIELD_CLASS.equals(pd.getName())) {
                try {
                    Object val = vf.val(fm != null ? fm.map(pd.getName()) : pd.getName());
                    pd.getWriteMethod().invoke(t, val);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        });
        return t;
    }
}
