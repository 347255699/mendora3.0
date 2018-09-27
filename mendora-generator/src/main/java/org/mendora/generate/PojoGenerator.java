package org.mendora.generate;

import com.squareup.javapoet.*;
import lombok.extern.slf4j.Slf4j;
import org.mendora.generate.config.Config;
import org.mendora.generate.jdbc.JdbcDriver;
import org.mendora.generate.jdbc.TableDesc;
import org.mendora.generate.util.LombokAnnotation;
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
                // 取得表格信息, 生成pojo
                TypeSpec.Builder pojoBuilder = TypeSpec.classBuilder(StringUtils.firstLetterToUpperCase(StringUtils.lineToHump(name)))
                        .addModifiers(Modifier.PUBLIC);
                List<TableDesc> tds = JdbcDriver.newDriver().desc(name);
                tds.forEach(td ->
                        parseType(td.getType()).ifPresent(type -> {
                            FieldSpec field = FieldSpec.builder(type, StringUtils.lineToHump(td.getField()))
                                    .addModifiers(Modifier.PRIVATE)
                                    .addAnnotation(lombok(LombokAnnotation.GETTER))
                                    .build();
                            pojoBuilder.addField(field);
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
