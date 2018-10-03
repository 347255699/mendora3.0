package org.mendora.course;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/10/3
 * desc:
 */
@Slf4j
public class SocketClient {
    public static void main(String[] args) {
        try {
            SocketChannel sc = SocketChannel.open();
            sc.connect(new InetSocketAddress("localhost", 8080));
            if (sc.finishConnect()) {
                String msg = "hello";
                final ByteBuffer writeBuf = ByteBuffer.allocate(msg.getBytes().length);
                while (true) {
                    writeBuf.put(msg.getBytes());
                    writeBuf.flip();
                    sc.write(writeBuf);
                    Thread.currentThread().sleep(3000);
                    writeBuf.clear();
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
}
