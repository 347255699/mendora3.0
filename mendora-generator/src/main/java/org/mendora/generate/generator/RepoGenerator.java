package org.mendora.generate.generator;

import com.squareup.javapoet.TypeSpec;
import org.mendora.generate.config.Config;
import org.mendora.generate.lombok.ConstructorAnnotation;
import org.mendora.generate.lombok.LombokAnnotation;

import javax.lang.model.element.Modifier;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/28
 * desc:
 */
class RepoGenerator implements Generator {

    private RepoGenerator() {
    }

    public static RepoGenerator newGenerator() {
        return new RepoGenerator();
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
        if (Config.pojoConfig().isToStringAnnotation()) {
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

    @Override
    public void generate() {

    }
}
