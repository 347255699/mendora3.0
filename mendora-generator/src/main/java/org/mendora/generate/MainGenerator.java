package org.mendora.generate;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import lombok.extern.slf4j.Slf4j;
import org.mendora.generate.director.Director;
import org.mendora.generate.generator.GeneratorFactory;
import org.mendora.generate.jdbc.JdbcDriver;
import org.mendora.generate.jdbc.TableDesc;
import org.mendora.generate.util.StringUtils;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/26
 * showFullColumns:
 */
@Slf4j
public class MainGenerator {
    private static TypeSpec generatePojo(String pojoName, List<TableDesc> tds) {
        return GeneratorFactory.product(GeneratorFactory.Type.POJO).generate(pojoName, tds);
    }

    private static TypeSpec generateRepoInterface(String pojoName, List<TableDesc> tds) {
        return GeneratorFactory.product(GeneratorFactory.Type.REPO_INTERFACE).generate(pojoName, tds);
    }

    private static TypeSpec generateRepoImplement(String pojoName, List<TableDesc> tds) {
        return GeneratorFactory.product(GeneratorFactory.Type.REPO_IMPLEMENT).generate(pojoName, tds);
    }

    public static void generate() {
        Arrays.asList(Director.tables()).forEach(tableName -> {
            String pojoName = StringUtils.firstLetterToUpperCase(StringUtils.lineToHump(tableName));
            try {
                List<TableDesc> tds = JdbcDriver.newDriver().showFullColumns(tableName);

                // 生成pojo
                TypeSpec pojoTypeSpec = generatePojo(pojoName, tds);
                JavaFile pojoJavaFile = JavaFile.builder(Director.pojoDirector().getPackageName(), pojoTypeSpec).build();
                pojoJavaFile.writeTo(Paths.get(Director.targetPath()));

                // 生成repository interface
                TypeSpec repoInterfaceTypeSpec = generateRepoInterface(pojoName, tds);
                JavaFile repoInterfaceJavaFile = JavaFile.builder(Director.repoDirector().getPackageName(), repoInterfaceTypeSpec).build();
                repoInterfaceJavaFile.writeTo(Paths.get(Director.targetPath()));

                // 生成repository implement
                TypeSpec repoImplementTypeSpec = generateRepoImplement(pojoName, tds);
                JavaFile repoImplementJavaFile = JavaFile.builder(Director.repoDirector().getPackageName() + ".impl", repoImplementTypeSpec).build();
                repoImplementJavaFile.writeTo(Paths.get(Director.targetPath()));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    public static void main(String[] args) {
        generate();
    }
}
