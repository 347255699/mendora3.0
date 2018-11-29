package org.mendora.generate.generator;

import com.squareup.javapoet.*;
import lombok.extern.slf4j.Slf4j;
import org.mendora.generate.director.Director;
import org.mendora.generate.director.DirectorFactory;
import org.mendora.generate.director.PojoDirector;
import org.mendora.generate.director.RepoDirector;
import org.mendora.generate.jdbc.TableDesc;
import org.mendora.generate.lombok.LombokAnnotation;
import org.mendora.generate.util.PrimaryKeyType;
import org.mendora.generate.util.StringUtils;

import javax.lang.model.element.Modifier;
import java.util.List;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/29
 * desc:
 */
@Slf4j
public class RepoImplementGenerator implements Generator {

    private RepoImplementGenerator() {
    }

    static RepoImplementGenerator newGenerator() {
        return new RepoImplementGenerator();
    }

    @Override
    public TypeSpec generate(String pojoName, List<TableDesc> tds) {
        return implementTypeSpecBuilder(pojoName).build();
    }

    private TypeSpec.Builder implementTypeSpecBuilder(String pojoName) {
        Director director = DirectorFactory.director();
        RepoDirector repoDirector = director.getRepoDirector();
        PojoDirector pojoDirector = director.getPojoDirector();
        TypeSpec.Builder repoImplBuilder = TypeSpec.classBuilder(pojoName + "RepositoryImpl")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ClassName.get(repoDirector.getPackageName(), pojoName + "Repository"))
                .addAnnotation(AnnotationSpec.builder(ClassName.get("org.springframework.stereotype", "Repository")).build());
        addGenerateComment(repoImplBuilder);
        if (repoDirector.getImplementDirector().isSlf4jAnnotation()) {
            repoImplBuilder.addAnnotation(lombok(LombokAnnotation.SLF4J));
        }
        PrimaryKeyType.valOf(repoDirector.getPrimaryKeyType()).ifPresent(pkt -> {
            ParameterizedTypeName repoClass = ParameterizedTypeName.get(
                    ClassName.get(repoDirector.getSuperRepoPackage() + ".impl", repoDirector.getImplementDirector().getSuperClass()),
                    ClassName.get(pojoDirector.getPackageName(), pojoName),
                    pkt.typeName
            );
            repoImplBuilder.superclass(repoClass);
        });
        FieldSpec tableName = FieldSpec.builder(String.class, "TABLE_NAME", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("\"" + StringUtils.humpToLine2(pojoName).substring(1) + "\"")
                .build();

        repoImplBuilder.addField(tableName);

        ParameterizedTypeName returnType = ParameterizedTypeName.get(ClassName.get(Class.class), ClassName.get(pojoDirector.getPackageName(), pojoName));

        MethodSpec getBeanClassMethod = MethodSpec.methodBuilder("getBeanClass")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(returnType)
                .addCode("return " + pojoName + ".class;")
                .build();

        MethodSpec getTableName = MethodSpec.methodBuilder("getTableName")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(String.class)
                .addCode("return TABLE_NAME;")
                .build();

        return repoImplBuilder.addMethod(getBeanClassMethod).addMethod(getTableName);
    }
}
