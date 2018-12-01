package org.mendora.io;


import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

public class RandomAccessFileDemo {
    public static void main(String[] args) {
        String filePath = "/Users/menfre/Workbench/project/mendora3.0/mendora-course/src/main/resources/demo.txt";
        RandomAccessFile raFile = null;
        try {
            raFile = new RandomAccessFile(filePath, "rw");
            final byte[] bytes = new byte[1024];
            raFile.writeBytes("Hello my world.");
            raFile.read(bytes);
            System.out.println(new String(bytes, Charset.forName("utf-8")));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            if (raFile != null) {
                try {
                    raFile.close();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }
}
