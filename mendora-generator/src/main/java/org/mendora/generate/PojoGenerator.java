package org.mendora.generate;

import com.squareup.javapoet.*;
import lombok.extern.slf4j.Slf4j;
import org.mendora.generate.config.Config;
import org.mendora.generate.jdbc.JdbcDriver;
import org.mendora.generate.jdbc.TableDesc;
import org.mendora.generate.util.LombokAnnotation;
import org.mendora.generate.util.StringUtils;

import javax.lang.model.element.Modifier;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
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
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(lombok(LombokAnnotation.TO_STRING));
                List<TableDesc> tds = JdbcDriver.newDriver().desc(name);
                tds.forEach(td ->
                        parseType(td.getType()).ifPresent(type -> {
                            // 字段名称
                            String pojoField = StringUtils.lineToHump(td.getField());

                            // 构造setter方法
                            MethodSpec setter = MethodSpec.methodBuilder("set" + StringUtils.firstLetterToUpperCase(pojoField))
                                    .addModifiers(Modifier.PUBLIC)
                                    .returns(ClassName.get(Config.pojoPackage(), pojoName))
                                    .addParameter(type, pojoField)
                                    .addStatement("this.$L = $L", pojoField, pojoField)
                                    .addStatement("return this")
                                    .build();

                            // 构造成员属性
                            FieldSpec field = FieldSpec.builder(type, pojoField)
                                    .addModifiers(Modifier.PRIVATE)
                                    .addAnnotation(lombok(LombokAnnotation.GETTER))
                                    .build();

                            pojoBuilder.addField(field).addMethod(setter);
                        })
                );
                JavaFile javaFile = JavaFile.builder(Config.pojoPackage(), pojoBuilder.build()).build();
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

    public static void main(String[] args) {
        try {
            PojoGenerator.newGenerator().generate();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
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
