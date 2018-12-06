package org.mendora.io;

import java.io.RandomAccessFile;
import java.nio.charset.Charset;

/**
 * @author menfre
 */
public class RandomAccessFileDemo {
    public static void main(String[] args) throws Exception {
        String filePath = "/Users/menfre/Workbench/project/mendora3.0/mendora-course/src/main/resources/demo.txt";
        try (RandomAccessFile raFile = new RandomAccessFile(filePath, "rw")) {
            final byte[] bytes = new byte[1024];
            raFile.writeBytes("Hello my world.");
            raFile.read(bytes);
            System.out.println(new String(bytes, Charset.forName("utf-8")));
        }
    }
}
