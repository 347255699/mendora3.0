package org.mendora.io;

import lombok.extern.slf4j.Slf4j;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/10/3
 * desc:
 */
@Slf4j
public class ApplicationMain {
    public static void main(String[] args) {
        try {
            ServerReactor.newServerReactor(8080).open();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
