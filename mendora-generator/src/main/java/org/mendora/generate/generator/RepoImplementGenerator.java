package org.mendora.generate.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import lombok.extern.slf4j.Slf4j;
import org.mendora.generate.director.Director;
import org.mendora.generate.director.PojoDirector;
import org.mendora.generate.director.RepoDirector;
import org.mendora.generate.jdbc.TableDesc;

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

    public static RepoImplementGenerator newGenerator() {
        return new RepoImplementGenerator();
    }

    @Override
    public TypeSpec generate(String pojoName, List<TableDesc> tds) {
        return implementTypeSpecBuilder(pojoName).build();
    }

    private TypeSpec.Builder implementTypeSpecBuilder(String pojoName) {
        RepoDirector repoDirector = Director.repoDirector();
        PojoDirector pojoDirector = Director.pojoDirector();
        ParameterizedTypeName repoClass = null;
        try {
            repoClass = ParameterizedTypeName.get(
                    ClassName.get(repoDirector.getSuperRepoPackage() + ".impl", repoDirector.getImplementDirector().getSuperClass()),
                    ClassName.get(Class.forName(repoDirector.getPrimaryKeyType())),
                    ClassName.get(pojoDirector.getPackageName(), pojoName)
            );
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        }
        return TypeSpec.classBuilder(pojoName + "RepositoryImpl")
                .addModifiers(Modifier.PUBLIC)
                .superclass(repoClass)
                .addSuperinterface(ClassName.get(repoDirector.getPackageName(), pojoName + "Repository"));
    }
}
