package org.mendora.io;

import java.io.*;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/12/6
 * desc:
 */
public class ReaderDemo {
    private static final String READ_PATH = "/Users/pundix043/Desktop/io.txt";
    private static final String WRITE_PATH = "/Users/pundix043/Desktop/io2.txt";
    private static final boolean APPEND_MODE = true;

    public void read(String path) {
        read(new File(path));
    }

    public void read(File file) {
        try (final Reader reader = new InputStreamReader(new FileInputStream(file))) {
            final StringBuilder sb = new StringBuilder();
            final char[] chars = new char[8];
            int read = reader.read(chars);
            while (-1 != read) {
                // 处理数据
                sb.append(chars);
                read = reader.read(chars);
            }
            String result = sb.toString().trim();
            System.out.println("string size: " + result.length());
            System.out.println(sb.toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void write(String path) {
        final File file = new File(path);
        boolean exists = file.exists();
        if (!exists) {
            try {
                exists = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (exists) {
            write(file);
        }
    }

    public void write(File file) {
        try (final Writer writer = new OutputStreamWriter(new FileOutputStream(file, APPEND_MODE))) {
            writer.write("hello, this your new life.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ReaderDemo().read(READ_PATH);
        new ReaderDemo().write(WRITE_PATH);
    }
}
