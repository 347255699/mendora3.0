package org.mendora.io;

import lombok.extern.slf4j.Slf4j;
import org.mendora.io.loop.AcceptLoop;
import org.mendora.io.loop.LoopSelectorProvider;
import org.mendora.io.selection.SelectionAcceptHandler;
import org.mendora.io.selection.SelectionReadHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
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
     * 服务端通道
     */
    private ServerSocketChannel ssc;
    /**
     * Selector提供者
     */
    private LoopSelectorProvider selectorProvider;

    private ServerReactor(int port) {
        try {
            ssc = ServerSocketChannel.open();
            // 绑定端口
            ssc.socket().bind(new InetSocketAddress(port));
            // 设置非阻塞模式以启用Selector
            ssc.configureBlocking(false);
            selectorProvider = LoopSelectorProvider.newLoopSelectorProvider();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static ServerReactor newServerReactor(int port) {
        return new ServerReactor(port);
    }

    /**
     * 开启反应器
     *
     * @param acceptHandler 链接接入处理器
     * @param readHandler   读处理器
     * @throws Exception 异常
     */
    public void open(SelectionAcceptHandler acceptHandler, SelectionReadHandler readHandler) throws Exception {
        // 注册链接接入事件
        ssc.register(selectorProvider.acceptor(), SelectionKey.OP_ACCEPT);
        // 开启链接接入监听循环
        AcceptLoop.newAcceptLoop(selectorProvider, acceptHandler, readHandler).start();
    }

}
