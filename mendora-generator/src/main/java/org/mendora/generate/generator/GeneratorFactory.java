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
         * repository interface
         */
        REPO_INTERFACE,
        /**
         * repository implement
         */
        REPO_IMPLEMENT
    }

    /**
     * 生产
     *
     * @param type 生成器类型
     * @return 生成器
     */
    public static Generator generator(Type type) {
        switch (type) {
            case POJO:
                return PojoGenerator.newGenerator();
            case REPO_INTERFACE:
                return RepoInterfaceGenerator.newGenerator();
            case REPO_IMPLEMENT:
                return RepoImplementGenerator.newGenerator();
            default:
                return PojoGenerator.newGenerator();
        }
    }
}
