package org.mendora.generate.director;

import lombok.Data;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/27
 * desc:
 */
@Data
public class PojoDirector {
    private String packageName;
    private boolean chainMode = true;
    private boolean dataAnnotation = false;
    private String constructor;
    private boolean toStringAnnotation = false;
    private boolean slf4jAnnotation = false;
    private String[] nonNull;
}