package org.mendora.generate;

import lombok.extern.slf4j.Slf4j;
import org.mendora.generate.generator.GeneratorFactory;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/26
 * desc:
 */
@Slf4j
public class MainGenerator {
    public static void generatePojo() {
        GeneratorFactory.product(GeneratorFactory.Type.POJO).generate();
    }

    public static void main(String[] args) {
        // pojo
        generatePojo();
    }
}
