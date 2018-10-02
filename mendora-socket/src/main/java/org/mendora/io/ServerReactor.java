package org.mendora.io;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/10/2
 * desc:
 */
@Slf4j
public class ServerReactor {
    /**
     * 监听端口号
     */
    private int port;
    private ServerSocketChannel ssc;

    private ServerReactor(int port){
        this.port = port;
        try {
            ssc = ServerSocketChannel.open();
            ssc.socket().bind(new InetSocketAddress(port));
            ssc.configureBlocking(false);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static ServerReactor newServerReactor(int port){
        return new ServerReactor(port);
    }

    public void start(){

    }

}
