package org.mendora.io;

import lombok.extern.slf4j.Slf4j;
import org.mendora.io.decoder.ReadDecoder;
import org.mendora.io.loop.LoopProvider;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/21
 * desc: reactor of socket event.
 */
@Slf4j
public class Reactor {
    /**
     * server socket channel, when a connect reached using this channel accept.
     */
    private ServerSocketChannel ssc;
    private Reactor(int port) {
        try {
            ssc = ServerSocketChannel.open();
            ssc.socket().bind(new InetSocketAddress(port));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static Reactor newReactor(int port) {
        return new Reactor(port);
    }

    /**
     * open server socket.
     *
     * @throws IOException when io operation failed throw it.
     */
    public void open(ReadDecoder readDecoder) throws Exception {
        LoopProvider.newLoopProvider().execute(ssc, readDecoder);
    }

}
