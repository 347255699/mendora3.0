package org.mendora.io;

import lombok.extern.slf4j.Slf4j;
import org.mendora.io.handler.ConnectOrAcceptHandler;
import org.mendora.io.handler.ReadHandler;
import org.mendora.io.loop.LoopProvider;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/21
 * desc: 客户端反应器
 */
@Slf4j
public class ClientReactor {
    // 套接字通道
    private SocketChannel sc;

    /**
     * 远端地址
     *
     * @param remoteAdress
     */
    private ClientReactor(InetSocketAddress remoteAdress) {
        try {
            sc = SocketChannel.open();
            sc.configureBlocking(false);
            // 发起链接
            sc.connect(remoteAdress);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static ClientReactor newReactor(InetSocketAddress remoteAdress) {
        return new ClientReactor(remoteAdress);
    }

    /**
     * 启动客户端反应器
     *
     * @param connectedHandler 链接建立完成后触发该处理器
     * @param readHandler      消息到达并可读时触发该处理器
     * @throws Exception
     */
    public void open(ConnectOrAcceptHandler connectedHandler, ReadHandler readHandler) throws Exception {
        // 启动多个循环器
        LoopProvider.newLoopProvider().execute(sc, connectedHandler, readHandler);
    }
}
