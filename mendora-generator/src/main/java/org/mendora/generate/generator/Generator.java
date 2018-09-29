package org.mendora.generate.generator;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import org.mendora.generate.jdbc.TableDesc;

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
     * @param name
     * @param packageName
     * @return
     */
    default AnnotationSpec lombok(String name, String packageName) {
        return AnnotationSpec.builder(ClassName.get(packageName, name))
                .build();
    }
}
