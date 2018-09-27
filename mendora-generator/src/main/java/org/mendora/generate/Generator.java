package org.mendora.generate;

import lombok.extern.slf4j.Slf4j;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/26
 * desc:
 */
@Slf4j
public class Generator {
    public static void generatePojo(){
        PojoGenerator.newGenerator().generate();
    }

    public static void main(String[] args) {
        // pojo
        generatePojo();
    }
}
