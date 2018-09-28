package org.mendora.generate.lombok;


import java.util.Optional;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/27
 * desc: 构造器注解
 */
public enum ConstructorAnnotation {
    /**
     * 所有参数
     */
    ALL_ARGS("allArgs", LombokAnnotation.ALL_ARGS_CONSTRUCTOR),
    /**
     * 无参构造器
     */
    NO_ARGS("noArgs", LombokAnnotation.NO_ARGS_CONSTRUCTOR),
    /**
     * 有参构造器, @NonNull
     */
    REQ_ARGS("reqArgs", LombokAnnotation.REQUIRED_ARGS_CONSTRUCTOR);
    public String val;
    public String name;

    ConstructorAnnotation(String val, String name) {
        this.val = val;
        this.name = name;
    }

    public static Optional<ConstructorAnnotation> valOf(String val) {
        ConstructorAnnotation ca = null;
        for (ConstructorAnnotation _ca : values()) {
            if (_ca.val.equals(val)) {
                ca = _ca;
            }
        }
        return Optional.ofNullable(ca);
    }
}
