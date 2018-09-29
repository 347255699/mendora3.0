package org.mendora.generate.lombok;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/27
 * desc: Lombok注解名称
 */
public interface LombokAnnotation {
    String GETTER = "Getter";
    String SETTER = "Setter";
    String TO_STRING = "ToString";
    String DATA = "Data";
    String NON_NULL = "NonNull";
    String BUILDER = "Builder";
    String SLF4J = "Slf4j";
    String REQUIRED_ARGS_CONSTRUCTOR = "RequiredArgsConstructor";
    String ALL_ARGS_CONSTRUCTOR = "AllArgsConstructor";
    String NO_ARGS_CONSTRUCTOR = "NoArgsConstructor";
}
