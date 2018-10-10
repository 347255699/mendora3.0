package org.mendora.generate;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import lombok.extern.slf4j.Slf4j;
import org.mendora.generate.director.Director;
import org.mendora.generate.director.DirectorFactory;
import org.mendora.generate.director.PojoDirector;
import org.mendora.generate.director.RepoDirector;
import org.mendora.generate.generator.GeneratorFactory;
import org.mendora.generate.jdbc.JdbcDriver;
import org.mendora.generate.jdbc.TableDesc;
import org.mendora.generate.util.PathUtils;
import org.mendora.generate.util.StringUtils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/26
 * desc:
 */
@Slf4j
public class MainGenerator {
    private final static JdbcDriver JDBC_DRIVER = JdbcDriver.newDriver();

    public static void init(byte[] bytes) {
        DirectorFactory.init(bytes);
    }

    public static boolean jdbcConnectTesting() {
        return JDBC_DRIVER.connectTesting();
    }

    public static void generate() {
        try {
            JDBC_DRIVER.showTables()
                    .stream()
                    .filter(tableName -> {
                        boolean isWeNeed = false;
                        final String[] tablePrefixs = DirectorFactory.director().getTablePrefixs();
                        for (String tablePrefix : tablePrefixs) {
                            if (tableName.startsWith(tablePrefix)) {
                                isWeNeed = true;
                                break;
                            }
                        }
                        return isWeNeed;
                    })
                    .forEach(MainGenerator::generate);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private static void generate(String tableName) {
        final Director director = DirectorFactory.director();
        final PojoDirector pojoDirector = director.getPojoDirector();
        final RepoDirector repoDirector = director.getRepoDirector();
        String pojoName = StringUtils.firstLetterToUpperCase(StringUtils.lineToHump(tableName));
        try {
            List<TableDesc> tds = JDBC_DRIVER.showFullColumns(tableName);
            Arrays.asList(GeneratorFactory.Type.values()).forEach(type -> {
                TypeSpec typeSpec = GeneratorFactory.generator(type).generate(pojoName, tds);
                String packageName;
                if(GeneratorFactory.Type.REPO_INTERFACE.equals(type)){
                    packageName = repoDirector.getPackageName();
                }else if (GeneratorFactory.Type.REPO_IMPLEMENT.equals(type)){
                    packageName = repoDirector.getPackageName() + ".impl";
                }else{
                    packageName = pojoDirector.getPackageName();
                }
                JavaFile javaFile = JavaFile.builder(packageName, typeSpec).build();
                try {
                    javaFile.writeTo(Paths.get(director.getTargetPath()));
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        String path = PathUtils.root() + "director.json";
        final byte[] bytes = new byte[2048];
        try {
            RandomAccessFile file = new RandomAccessFile(path, "r");
            FileChannel channel = file.getChannel();
            final ByteBuffer buf = ByteBuffer.allocate(bytes.length);
            int read = channel.read(buf);
            if (read > 0) {
                buf.flip();
                buf.get(bytes, 0, buf.limit());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        init(bytes);
        if (jdbcConnectTesting()) {
            generate();
            log.info("generate success!");
        } else {
            log.info("The database can't connected!");
        }
    }
}
