package org.mendora.generate.config;

import lombok.Data;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/27
 * desc:
 */
@Data
public class PojoConfig {
    private String packageName;
    private boolean chainMode = true;
    private boolean dataAnnotation = false;
    private String constructor;
    private String[] nonNull;
    private boolean toStringAnnotation = false;
}
