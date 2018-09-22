package org.mendora.io;

import lombok.extern.slf4j.Slf4j;
import java.nio.ByteBuffer;

/**
 * @author menfre
 * date: 2018/9/22
 * version: 1.0
 * desc:
 */
@Slf4j
public class ApplicationMain {
    public static void main(String[] args) {
        try {
            log.info("socket启动！");
            Reactor.newReactor(8080).open(ctx -> {
                // if need to holding inter status
                Object something = null;
                ctx.setAttachment(something);

                // if need to using inter status.
                Object obj = ctx.attachment();

                // use readBuf do something
                ByteBuffer readBuf = ctx.getReadBuf();

                // need to write something wo socket
                ByteBuffer writeBuf = ByteBuffer.allocate(1024);
                ctx.write(writeBuf);

                // chose short or long socket
                ctx.close();
                return true;
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
