package org.mendora.io;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/10/3
 * desc:
 */
@Slf4j
public class ApplicationMain {
    public static void main(String[] args) {
        try {
            ServerReactor.newServerReactor(8080).open(se -> {
                InetSocketAddress remoteAddress = se.getRemoteAddress();
                log.info("channel created. from: {}", remoteAddress);
            }, se -> {
                final ByteBuffer readBuf = se.getReadBuf();
                final byte[] bytes = new byte[1024];
                readBuf.get(bytes, 0, readBuf.limit());
                log.info(new String(bytes));
                String respMsg = "hi, i'am menfre.";
                final ByteBuffer writeBuf = ByteBuffer.allocate(respMsg.getBytes().length);
                writeBuf.put(respMsg.getBytes());
                se.write(writeBuf);
                se.keepAlive();
                return true;
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
