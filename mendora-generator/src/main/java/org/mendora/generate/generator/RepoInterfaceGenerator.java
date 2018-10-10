package org.mendora.generate.generator;

import com.squareup.javapoet.*;
import lombok.extern.slf4j.Slf4j;
import org.mendora.generate.director.Director;
import org.mendora.generate.director.DirectorFactory;
import org.mendora.generate.director.PojoDirector;
import org.mendora.generate.director.RepoDirector;
import org.mendora.generate.jdbc.TableDesc;
import org.mendora.generate.util.PrimaryKeyType;

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

    private TypeSpec.Builder interfaceTypeSpecBuilder(String pojoName) {
        Director director = DirectorFactory.director();
        RepoDirector repoDirector = director.getRepoDirector();
        PojoDirector pojoDirector = director.getPojoDirector();
        TypeSpec.Builder repoInterfaceBuilder = TypeSpec.interfaceBuilder(pojoName + "Repository")
                .addModifiers(Modifier.PUBLIC);
        addGenerateComment(repoInterfaceBuilder);
        PrimaryKeyType.valOf(repoDirector.getPrimaryKeyType()).ifPresent(pkt -> {
            ParameterizedTypeName repoInterface = ParameterizedTypeName.get(
                    ClassName.get(repoDirector.getSuperRepoPackage(), repoDirector.getInterfaceDirector().getSuperInterface()),
                    pkt.typeName,
                    ClassName.get(pojoDirector.getPackageName(), pojoName)
            );
            repoInterfaceBuilder.addSuperinterface(repoInterface);
        });
        return repoInterfaceBuilder;
    }

    static RepoInterfaceGenerator newGenerator() {
        return new RepoInterfaceGenerator();
    }

    @Override
    public TypeSpec generate(String pojoName, List<TableDesc> tds) {
        return interfaceTypeSpecBuilder(pojoName).build();
    }
}
