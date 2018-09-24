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
    // example
    public static void main(String[] args) {
        try {
            // 打开反应器
            ServerReactor.newReactor(10000).open(
                    ctx -> {
                        // 有链接接入
                        log.info("客户端接入，地址：{}", ctx.getRemoteAddress());
                    }
                    , ctx -> {
//                        // 自定义反应器内部上下文
//                        Object context = null;
//                        ctx.setAttachment(context);
//
//                        // 取出上下文
//                        Object obj = ctx.attachment();

                        // 根据上下文做一些事情

                        // 取得可读缓冲区
                        ByteBuffer readBuf = ctx.getReadBuf();
                        byte[] data = new byte[1024];
                        readBuf.get(data);
                        log.info("数据到达，消息：{}", new String(data));
                        // 写出消息
//                        ByteBuffer writeBuf = ByteBuffer.allocate(1024);
//                        ctx.write(writeBuf);
//
//                        // 关闭连接(短连接)
//                        ctx.close();

                        // 告诉反应器此处数据是否接收或处理成功(以此来维护粘包和断包的情况)
                        return true;
                    });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
