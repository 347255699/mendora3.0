package org.mendora.generate.director;

import lombok.Data;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/29
 * desc:
 */
@Data
public class RepoDirector {
    private String packageName;
    private String primaryKeyType;
    private String superRepoPackage;
    private RepoInterfaceDirector interfaceDirector;
    private RepoImplementDirector implementDirector;
}
