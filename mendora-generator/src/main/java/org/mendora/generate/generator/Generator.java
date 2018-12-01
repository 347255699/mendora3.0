package org.mendora.generate.generator;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import org.mendora.generate.jdbc.TableDesc;
import org.mendora.generate.lombok.LombokAnnotation;

import java.time.LocalDateTime;
import java.util.List;


/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/28
 * desc:
 */
public interface Generator {
    String LOMBOK_PACKAGE = "lombok";
    String LOMBOK_EXTERN_SLF4J_PACKAGE = "lombok.extern.slf4j";
    String SWAGGER_PACKAGE = "io.swagger.annotations";

    /**
     * 生成
     *
     * @param pojoName pojo名称
     * @param tds      表结构描述
     * @return 类型描述列表
     */
    TypeSpec generate(String pojoName, List<TableDesc> tds);

    /**
     * 添加lombok注解
     *
     * @param name lombok注解名称
     * @return 注解描述者
     */
    default AnnotationSpec lombok(String name) {
        String packageName = LombokAnnotation.SLF4J.equals(name) ? LOMBOK_EXTERN_SLF4J_PACKAGE : LOMBOK_PACKAGE;
        return AnnotationSpec.builder(ClassName.get(packageName, name))
                .build();
    }

    /**
     * 添加swagger注解
     *
     * @param name swagger注解名称
     * @return 注解描述者
     */
    default AnnotationSpec swagger(String name) {
        return AnnotationSpec.builder(ClassName.get(SWAGGER_PACKAGE, name))
                .addMember("value", "")
                .build();
    }

    /**
     * 添加swagger注解
     *
     * @param name    swagger注解名称
     * @param comment swagger注释
     * @return 注解描述者
     */
    default AnnotationSpec swagger(String name, String comment) {
        return AnnotationSpec.builder(ClassName.get(SWAGGER_PACKAGE, name))
                .addMember("value", comment)
                .build();
    }

    /**
     * 添加注释
     *
     * @param typeSpecBuilder 类描述构建者
     */
    default void addGenerateComment(TypeSpec.Builder typeSpecBuilder) {
        typeSpecBuilder
                .addJavadoc("@author generate from mendora-generator\n")
                .addJavadoc("@version 1.0\n")
                .addJavadoc("date: " + LocalDateTime.now() + "\n")
                .addJavadoc("desc:");
    }
}
