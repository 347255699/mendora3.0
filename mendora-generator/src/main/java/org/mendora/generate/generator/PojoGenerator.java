package org.mendora.generate.generator;

import com.squareup.javapoet.*;
import lombok.extern.slf4j.Slf4j;
import org.mendora.generate.config.Config;
import org.mendora.generate.jdbc.JdbcDriver;
import org.mendora.generate.jdbc.TableDesc;
import org.mendora.generate.lombok.ConstructorAnnotation;
import org.mendora.generate.lombok.LombokAnnotation;
import org.mendora.generate.util.StringUtils;

import javax.lang.model.element.Modifier;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/26
 * desc: Pojo生成器
 */
@Slf4j
class PojoGenerator implements Generator {
    private static final String CONTENT_INTEGER = "int";
    private static final String CONTENT_VARCHAR = "varchar";
    private static final String CONTENT_TEXT = "text";
    private static final String CONTENT_BIG = "big";
    private static final String CONTENT_SHORT = "tiny";

    private PojoGenerator() {
    }

    static PojoGenerator newGenerator() {
        return new PojoGenerator();
    }

    /**
     * 类型描述器
     *
     * @param pojoName Pojo名称
     * @return 类型构建者
     */
    private TypeSpec.Builder typeSpecBuilder(String pojoName) {
        // 取得表格信息, 生成pojo
        TypeSpec.Builder pojoBuilder = TypeSpec.classBuilder(pojoName)
                .addModifiers(Modifier.PUBLIC);
        if (Config.pojoConfig().isSlf4jAnnotation()) {
            pojoBuilder.addAnnotation(lombok(LombokAnnotation.SLF4J, LOMBOK_EXTERN_SLF4J_PACKAGE));
        }
        if (!Config.pojoConfig().isDataAnnotation()) {
            if (Config.pojoConfig().isToStringAnnotation()) {
                pojoBuilder.addAnnotation(lombok(LombokAnnotation.TO_STRING, LOMBOK_PACKAGE));
            }
            String constructor = Config.pojoConfig().getConstructor();
            if (constructor != null && constructor.length() > 0) {
                ConstructorAnnotation.valOf(constructor).ifPresent(ca -> pojoBuilder.addAnnotation(lombok(ca.name, LOMBOK_PACKAGE)));
            }
        } else {
            pojoBuilder.addAnnotation(lombok(LombokAnnotation.DATA, LOMBOK_PACKAGE));
        }
        return pojoBuilder;
    }

    /**
     * 方法描述器
     *
     * @param pojoName   Pojo名称
     * @param methodName 方法名称
     * @param type       属性类型
     * @param pojoField  属性名称
     * @return 方法构建者
     */
    private MethodSpec.Builder methodSpecBuilder(String pojoName, String methodName, Class<?> type, String pojoField) {
        // 构造setter方法
        MethodSpec.Builder setterBuilder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(Config.pojoConfig().getPackageName(), pojoName))
                .addParameter(type, pojoField)
                .addStatement("this.$L = $L", pojoField, pojoField)
                .addStatement("return this");
        return setterBuilder;
    }

    /**
     * 字段描述器
     *
     * @param type      字段类型
     * @param pojoField 字段名称
     * @param comment   注释
     */
    private FieldSpec.Builder fieldSpecBuilder(Class<?> type, String pojoField, String comment) {
        // 构造成员属性
        FieldSpec.Builder fieldBuilder = FieldSpec.builder(type, pojoField)
                .addModifiers(Modifier.PRIVATE);
        if (comment != null && comment.length() > 0) {
            fieldBuilder.addJavadoc(comment);
        }
        if (!Config.pojoConfig().isDataAnnotation()) {
            fieldBuilder.addAnnotation(lombok(LombokAnnotation.GETTER, LOMBOK_PACKAGE));
            if (!Config.pojoConfig().isChainMode()) {
                fieldBuilder.addAnnotation(lombok(LombokAnnotation.SETTER, LOMBOK_PACKAGE));
            }
            if (Config.pojoConfig().getNonNull() != null) {
                Arrays.asList(Config.pojoConfig().getNonNull()).forEach(field -> {
                    if (field.equals(pojoField)) {
                        fieldBuilder.addAnnotation(lombok(LombokAnnotation.NON_NULL, LOMBOK_PACKAGE));
                    }
                });
            }
        }
        return fieldBuilder;
    }

    /**
     * 映射类型
     *
     * @param type java类型名称
     * @return java类型
     */
    private Optional<Class<?>> parseType(String type) {
        Class<?> clazz = null;
        if (type.contains(CONTENT_INTEGER)) {
            if (type.startsWith(CONTENT_BIG)) {
                clazz = long.class;
            } else if (type.startsWith(CONTENT_SHORT)) {
                clazz = short.class;
            } else {
                clazz = int.class;
            }
        }
        if (type.contains(CONTENT_VARCHAR) || CONTENT_TEXT.equals(type)) {
            clazz = String.class;
        }
        return Optional.of(clazz);
    }

    /**
     * 生成
     */
    @Override
    public void generate() {
        Arrays.asList(Config.tables()).forEach(name -> {
            try {
                String pojoName = StringUtils.firstLetterToUpperCase(StringUtils.lineToHump(name));
                TypeSpec.Builder pojoBuilder = typeSpecBuilder(pojoName);
                List<TableDesc> tds = JdbcDriver.newDriver().desc(name);
                tds.forEach(td ->
                        parseType(td.getType()).ifPresent(type -> {
                            // 字段名称
                            String pojoField = StringUtils.lineToHump(td.getField());
                            FieldSpec.Builder fieldBuilder = fieldSpecBuilder(type, pojoField, td.getComment());
                            pojoBuilder.addField(fieldBuilder.build());
                            if (Config.pojoConfig().isChainMode() && !Config.pojoConfig().isDataAnnotation()) {
                                String methodName = "set" + StringUtils.firstLetterToUpperCase(pojoField);
                                MethodSpec.Builder setterBuilder = methodSpecBuilder(pojoName, methodName, type, pojoField);
                                pojoBuilder.addMethod(setterBuilder.build());
                            }
                        })
                );
                JavaFile javaFile = JavaFile.builder(Config.pojoConfig().getPackageName(), pojoBuilder.build()).build();
                javaFile.writeTo(Paths.get(Config.targetPath()));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }
}
