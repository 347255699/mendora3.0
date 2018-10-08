package org.mendora.io.loop;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/21
 * desc: 抽象监听器
 */
@Slf4j
public abstract class AbstractLoop extends Thread {
    /**
     * 选择器
     */
    private Selector selector;

    /**
     * 事件就绪后触发
     *
     * @param selectionKey 选择键
     */
    protected abstract void handle(SelectionKey selectionKey);

    protected AbstractLoop(Selector selector) {
        this.selector = selector;
    }

    /**
     * 事件循环
     *
     * @param selectionKeys 选择键集合
     */
    private void eventLoop(Set<SelectionKey> selectionKeys) {
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey selectionKey = iterator.next();
            iterator.remove();
            // 处理事件
            handle(selectionKey);
        }
    }

    /**
     * 执行选择
     */
    protected void select() {
        while (selector.isOpen()) {
            try {
                int selected = selector.select();
                if (selected == 0) {
                    continue;
                }
                if (selector.isOpen()) {
                    eventLoop(selector.selectedKeys());
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    protected void cancel(SelectionKey selectionKey) {
        try {
            if (selectionKey.channel().isOpen()) {
                selectionKey.channel().close();
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        selectionKey.cancel();
    }
}
