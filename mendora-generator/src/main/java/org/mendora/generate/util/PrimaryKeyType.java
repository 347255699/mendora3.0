package org.mendora.generate.util;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.Optional;

/**
 * @author menfre
 * date: 2018/9/29
 * version: 1.0
 * desc:
 */
public enum PrimaryKeyType {
    /**
     * 整型
     */
    INTEGER("Integer", TypeName.INT.box()),
    /**
     * 字符串
     */
    STRING("String", ClassName.get(String.class));
    public String name;
    public TypeName typeName;

    PrimaryKeyType(String name, TypeName typeName) {
        this.typeName = typeName;
        this.name = name;
    }

    public static Optional<PrimaryKeyType> valOf(String name) {
        PrimaryKeyType pkt = null;
        for (PrimaryKeyType _pkt : values()) {
            if (_pkt.name.equals(name)) {
                pkt = _pkt;
            }
        }
        return Optional.ofNullable(pkt);
    }
}
