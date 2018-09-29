package org.mendora.generate.generator;

import com.squareup.javapoet.*;
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
 * date: 2018/9/28
 * desc:
 */
@Slf4j
class RepoInterfaceGenerator implements Generator {
    private RepoInterfaceGenerator() {
    }

    private TypeSpec.Builder interfaceTypeSpecBuilder(String pojoName) {
        RepoDirector repoDirector = Director.repoDirector();
        PojoDirector pojoDirector = Director.pojoDirector();
        ParameterizedTypeName repoInterface = null;
        try {
            repoInterface = ParameterizedTypeName.get(
                    ClassName.get(repoDirector.getSuperRepoPackage(), repoDirector.getInterfaceDirector().getSuperInterface()),
                    ClassName.get(Class.forName(repoDirector.getPrimaryKeyType())),
                    ClassName.get(pojoDirector.getPackageName(), pojoName)
            );
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        }
        return TypeSpec.interfaceBuilder(pojoName + "Repository")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(repoInterface);
    }

    static RepoInterfaceGenerator newGenerator() {
        return new RepoInterfaceGenerator();
    }

    @Override
    public TypeSpec generate(String pojoName, List<TableDesc> tds) {

        return interfaceTypeSpecBuilder(pojoName).build();

    }
}
