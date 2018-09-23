package org.mendora.io.handler;

import java.nio.channels.SocketChannel;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/21
 * desc:
 */
@FunctionalInterface
public interface AcceptOrConnectHandler {
    void handle(SocketChannel socketChannel) throws Exception;
}
