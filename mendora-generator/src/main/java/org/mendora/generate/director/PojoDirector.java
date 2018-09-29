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
    private boolean builderAnnotation = false;
    private boolean dataAnnotation = false;
    private String[] constructor;
    private boolean toStringAnnotation = false;
}
