package org.mendora.io;

import lombok.extern.slf4j.Slf4j;
import org.mendora.io.decoder.ReadDecoder;
import org.mendora.io.loop.LoopProvider;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/21
 * desc: reactor of socket event.
 */
@Slf4j
public class ClientReactor {
    /**
     * server socket channel, when a connect reached using this channel accept.
     */
    private SocketChannel sc;

    private ClientReactor(InetSocketAddress remoteAdress) {
        try {
            sc = SocketChannel.open();
            sc.configureBlocking(false);
            sc.connect(remoteAdress);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static ClientReactor newReactor(InetSocketAddress remoteAdress) {
        return new ClientReactor(remoteAdress);
    }

    /**
     * open server socket.
     *
     * @throws IOException when io operation failed throw it.
     */
    public void open(ReadDecoder readDecoder) throws Exception {
        LoopProvider.newLoopProvider().execute(sc, readDecoder);
    }
}
