package org.mendora.generate.generator;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;
import lombok.extern.slf4j.Slf4j;
import org.mendora.generate.director.DirectorFactory;
import org.mendora.generate.director.PojoDirector;
import org.mendora.generate.jdbc.TableDesc;
import org.mendora.generate.lombok.ConstructorAnnotation;
import org.mendora.generate.lombok.LombokAnnotation;
import org.mendora.generate.swagger.SwaggerAnnotation;
import org.mendora.generate.util.StringUtils;

import javax.lang.model.element.Modifier;
import java.math.BigDecimal;
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

    private PojoDirector pojoDirector;

    enum SqlToJavaType {
        /**
         * 整型数值
         */
        INT("int", int.class),
        /**
         * 可变长字符串
         */
        VARCHAT("varchar", String.class),
        /**
         * 文本类型
         */
        TEXT("text", String.class),
        /**
         * 长整型
         */
        BIGINT("bigint", long.class),
        /**
         * 短整型
         */
        TINYINT("tinyint", int.class),

        /**
         * 大浮点数
         */
        DECIMAL("decimal", BigDecimal.class);
        public String sqlType;
        public Class<?> javaType;

        SqlToJavaType(String sqlType, Class<?> javaType) {
            this.sqlType = sqlType;
            this.javaType = javaType;
        }

        public static Optional<SqlToJavaType> valOf(String sqlType) {
            SqlToJavaType type = null;
            for (SqlToJavaType _type : values()) {
                if (sqlType.startsWith(_type.sqlType)) {
                    type = _type;
                    break;
                }
            }
            return Optional.ofNullable(type);
        }
    }

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
    private TypeSpec.Builder classSpecBuilder(String pojoName) {
        pojoDirector = DirectorFactory.director().getPojoDirector();

        // 取得表格信息, 生成pojo
        TypeSpec.Builder pojoBuilder = TypeSpec.classBuilder(pojoName)
                .addModifiers(Modifier.PUBLIC);
        if (pojoDirector.isToStringAnnotation()) {
            pojoBuilder.addAnnotation(lombok(LombokAnnotation.TO_STRING));
        }
        String[] constructors = pojoDirector.getConstructor();
        if (constructors != null && constructors.length > 0) {
            Arrays.asList(constructors).forEach(constructor ->
                    ConstructorAnnotation.valOf(constructor).ifPresent(ca -> pojoBuilder.addAnnotation(lombok(ca.name)))
            );

        }
        if (pojoDirector.isDataAnnotation()) {
            pojoBuilder.addAnnotation(lombok(LombokAnnotation.DATA));
        }
        if (pojoDirector.isBuilderAnnotation()) {
            pojoBuilder.addAnnotation(lombok(LombokAnnotation.BUILDER));
        }
        return pojoBuilder;
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
            if (pojoDirector.isApiModelProperty()) {
                fieldBuilder.addAnnotation(swagger(SwaggerAnnotation.API_MODEL_PROPERTY, "\"" + comment + "\""));
            } else {
                fieldBuilder.addJavadoc(comment);
            }
        }
        return fieldBuilder;
    }

    /**
     * 生成
     *
     * @param pojoName pojo名称
     * @param tds      表结构描述
     */
    @Override
    public TypeSpec generate(String pojoName, List<TableDesc> tds) {
        TypeSpec.Builder pojoBuilder = classSpecBuilder(pojoName);
        addGenerateComment(pojoBuilder);
        tds.forEach(td ->
                SqlToJavaType.valOf(td.getType()).ifPresent(type -> {
                    // 字段名称
                    String pojoField = StringUtils.lineToHump(td.getField());
                    FieldSpec.Builder fieldBuilder = fieldSpecBuilder(type.javaType, pojoField, td.getComment());
                    pojoBuilder.addField(fieldBuilder.build());
                })
        );
        return pojoBuilder.build();
    }
}
