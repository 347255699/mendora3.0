package org.mendora.io;

import lombok.extern.slf4j.Slf4j;
import org.mendora.io.handler.AcceptHandler;
import org.mendora.io.handler.ReadHandler;
import org.mendora.io.loop.LoopProvider;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/21
 * desc: 服务端反应器
 */
@Slf4j
public class ServerReactor {
    /**
     * 服务端套接字通道
     */
    private ServerSocketChannel ssc;

    /**
     * 服务端绑定端口号
     *
     * @param port 端口号
     */
    private ServerReactor(int port) {
        try {
            ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            // 监听
            ssc.socket().bind(new InetSocketAddress(port));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static ServerReactor newReactor(int port) {
        return new ServerReactor(port);
    }

    /**
     * 启动服务端反应器
     *
     * @param acceptedHandler 链接接入后触发该处理器
     * @param readHandler     消息到达并可读时触发该处理器
     * @throws Exception
     */
    public void open(AcceptHandler acceptedHandler, ReadHandler readHandler) throws Exception {
        // 启动多个循环器
        LoopProvider.newLoopProvider().execute(ssc, acceptedHandler, readHandler);
    }
}
