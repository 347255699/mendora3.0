package org.mendora.io.loop;

import lombok.extern.slf4j.Slf4j;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author menfre
 * date: 2018/9/30
 * version: 1.0
 * desc:
 */
@Slf4j
public class AcceptLoop extends Thread {
    private Selector acceptSelector;

    private AcceptLoop(Selector acceptSelector) {
        this.acceptSelector = acceptSelector;
    }

    private void msgLoop(Set<SelectionKey> sks) throws Exception {
        Iterator<SelectionKey> iterator = sks.iterator();
        while (iterator.hasNext()) {
            SelectionKey sk = iterator.next();
            iterator.remove();
            if (sk.isWritable() && sk.isWritable()) {
                ServerSocketChannel ssc = (ServerSocketChannel) sk.channel();
                SocketChannel clientChannel = ssc.accept();
            }
        }
    }

    private void select() throws Exception {
        while (acceptSelector.isOpen()) {
            int selected = acceptSelector.select();
            if (selected == 0) {
                continue;
            }
            if (acceptSelector.isOpen()) {
                Set<SelectionKey> sks = acceptSelector.selectedKeys();
                msgLoop(sks);
            }
        }
    }

    public static AcceptLoop newAcceptLoop(Selector accepteSelector) {
        return new AcceptLoop(accepteSelector);
    }

    @Override
    public void run() {
        try {
            select();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
