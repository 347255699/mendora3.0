package org.mendora.course;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;
import lombok.extern.slf4j.Slf4j;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/10/3
 * desc:
 */
@Slf4j
public class SocketClient {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.createNetClient().connect(8080, "localhost", asc -> {
            if (asc.succeeded()) {
                NetSocket socket = asc.result();
                socket.handler(buf -> {
                    log.info(buf.toString());
                });
                vertx.setPeriodic(3000, l -> {
                    socket.write("hello");
                });
            }
        });
    }
}
