package org.mendora.io.selection;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mendora.io.Config.Config;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/10/3
 * desc:
 */
@RequiredArgsConstructor
public class SelectionEventContext {
    @Getter
    private final ByteBuffer readBuf = ByteBuffer.allocate(Config.READ_BUFFER_SIZE);
    @NonNull
    @Getter
    private InetSocketAddress remoteAddress;
    @Getter
    private ConcurrentLinkedQueue<ByteBuffer> writeQueue = new ConcurrentLinkedQueue<>();

    @Getter
    private boolean keepAlive = false;

    public void write(ByteBuffer writeBuf) {
        writeQueue.add(writeBuf);
    }

    public void close() {
        if (isKeepAlive()) {
            keepAlive = false;
        }
    }

    public void keepAlive(){
        keepAlive = true;
    }
}
