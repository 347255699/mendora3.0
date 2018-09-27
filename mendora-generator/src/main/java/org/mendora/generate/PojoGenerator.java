package org.mendora.generate;

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
 * desc:
 */
@Slf4j
public class PojoGenerator {
    private static final String CONTENT_INTEGER = "int";
    private static final String CONTENT_VARCHAR = "varchar";
    private static final String PREFIX = "big";
    private static final String LOMBOK_PACKAGE = "lombok";

    private PojoGenerator() {

    }

    public static PojoGenerator newGenerator() {
        return new PojoGenerator();
    }

    public void generate() {
        Arrays.asList(Config.tables()).forEach(name -> {
            try {
                String pojoName = StringUtils.firstLetterToUpperCase(StringUtils.lineToHump(name));
                // 取得表格信息, 生成pojo
                TypeSpec.Builder pojoBuilder = TypeSpec.classBuilder(pojoName)
                        .addModifiers(Modifier.PUBLIC);
                if (!Config.pojoConfig().isDataAnnotation()) {
                    if (Config.pojoConfig().isToStringAnnotation()) {
                        pojoBuilder.addAnnotation(lombok(LombokAnnotation.TO_STRING));
                    }
                    String constructor = Config.pojoConfig().getConstructor();
                    if (constructor != null && constructor.length() > 0) {
                        ConstructorAnnotation.valOf(constructor).ifPresent(ca -> pojoBuilder.addAnnotation(lombok(ca.name)));
                    }
                } else {
                    pojoBuilder.addAnnotation(lombok(LombokAnnotation.DATA));
                }
                List<TableDesc> tds = JdbcDriver.newDriver().desc(name);
                tds.forEach(td ->
                        parseType(td.getType()).ifPresent(type -> {
                            // 字段名称
                            String pojoField = StringUtils.lineToHump(td.getField());
                            // 构造成员属性
                            FieldSpec.Builder fieldBuilder = FieldSpec.builder(type, pojoField)
                                    .addModifiers(Modifier.PRIVATE);
                            if (!Config.pojoConfig().isDataAnnotation()) {
                                if (Config.pojoConfig().getNonNull() != null) {
                                    Arrays.asList(Config.pojoConfig().getNonNull()).forEach(field -> {
                                        if (field.equals(pojoField)) {
                                            fieldBuilder.addAnnotation(lombok(LombokAnnotation.NON_NULL));
                                        }
                                    });
                                }
                                fieldBuilder.addAnnotation(lombok(LombokAnnotation.GETTER));
                                if (Config.pojoConfig().isChainMode()) {
                                    // 构造setter方法
                                    MethodSpec setter = MethodSpec.methodBuilder("set" + StringUtils.firstLetterToUpperCase(pojoField))
                                            .addModifiers(Modifier.PUBLIC)
                                            .returns(ClassName.get(Config.pojoConfig().getPackageName(), pojoName))
                                            .addParameter(type, pojoField)
                                            .addStatement("this.$L = $L", pojoField, pojoField)
                                            .addStatement("return this")
                                            .build();
                                    pojoBuilder.addMethod(setter);
                                } else {
                                    fieldBuilder.addAnnotation(lombok((LombokAnnotation.SETTER)));
                                }
                            }
                            pojoBuilder.addField(fieldBuilder.build());
                        })
                );
                JavaFile javaFile = JavaFile.builder(Config.pojoConfig().getPackageName(), pojoBuilder.build()).build();
                javaFile.writeTo(Paths.get(Config.targetPath()));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    private AnnotationSpec lombok(String name) {
        return AnnotationSpec.builder(ClassName.get(LOMBOK_PACKAGE, name))
                .build();
    }

    private Optional<Class<?>> parseType(String type) {
        Class<?> clazz = null;
        if (type.contains(CONTENT_INTEGER)) {
            if (type.startsWith(PREFIX)) {
                clazz = long.class;
            } else {
                clazz = int.class;
            }
        }
        if (type.contains(CONTENT_VARCHAR)) {
            clazz = String.class;
        }
        return Optional.of(clazz);
    }
}
