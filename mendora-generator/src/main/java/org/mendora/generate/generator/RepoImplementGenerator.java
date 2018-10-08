package org.mendora.generate.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import lombok.extern.slf4j.Slf4j;
import org.mendora.generate.director.Director;
import org.mendora.generate.director.PojoDirector;
import org.mendora.generate.director.RepoDirector;
import org.mendora.generate.jdbc.TableDesc;
import org.mendora.generate.lombok.LombokAnnotation;
import org.mendora.generate.util.PrimaryKeyType;

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
        RepoDirector repoDirector = Director.repoDirector();
        PojoDirector pojoDirector = Director.pojoDirector();
        TypeSpec.Builder repoImplBuilder = TypeSpec.classBuilder(pojoName + "RepositoryImpl")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ClassName.get(repoDirector.getPackageName(), pojoName + "Repository"));
        addGenerateComment(repoImplBuilder);
        if (Director.repoDirector().getImplementDirector().isSlf4jAnnotation()) {
            repoImplBuilder.addAnnotation(lombok(LombokAnnotation.SLF4J, LOMBOK_EXTERN_SLF4J_PACKAGE));
        }
        PrimaryKeyType.valOf(repoDirector.getPrimaryKeyType()).ifPresent(pkt -> {
            ParameterizedTypeName repoClass = ParameterizedTypeName.get(
                    ClassName.get(repoDirector.getSuperRepoPackage() + ".impl", repoDirector.getImplementDirector().getSuperClass()),
                    pkt.typeName,
                    ClassName.get(pojoDirector.getPackageName(), pojoName)
            );
            repoImplBuilder.superclass(repoClass);
        });
        return repoImplBuilder;
    }
}
