package org.mendora.generate.generator;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/28
 * desc:
 */
public class GeneratorFactory {

    public enum Type {
        /**
         * pojo
         */
        POJO,
        /**
         * repository
         */
        REPO
    }

    /**
     * 生产
     *
     * @param type 生成器类型
     * @return 生成器
     */
    public static Generator product(Type type) {
        switch (type) {
            case POJO:
                return PojoGenerator.newGenerator();
            case REPO:
                return RepoGenerator.newGenerator();
            default:
                return PojoGenerator.newGenerator();
        }
    }
}
