package com.wufish.javalearning.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.RandomAccessFile;

import org.junit.jupiter.api.Test;

/**
 * @author wuzhijun05 <wuzhijun05@kuaishou.com>
 * Created on 2023-10-08
 */
public class FileIOTester {
    @Test
    public void testBufferByte() {
        try (FileInputStream fis = new FileInputStream("D:\\file01.txt");
             FileOutputStream fos = new FileOutputStream("D:\\file01_copy.txt");
             BufferedInputStream bfis = new BufferedInputStream(fis);
             BufferedOutputStream bops = new BufferedOutputStream(fos)
        ) {
            int hasRead;
            byte[] bytes = new byte[1024];
            //循环的读取文件，并写入到文件
            while ((hasRead = bfis.read(bytes)) != -1) {
                bops.write(bytes, 0, hasRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBufferChar() {
        //使用普通的 Reader 不方便整行读取,可以使用 BufferReader 包装,资源变量要定义在 try()中,否则不会自动关闭
        try (FileReader fr = new FileReader("D:\\file01.txt");
             FileWriter fw = new FileWriter("D:\\file01_copy2_2.txt");
             BufferedReader bfr = new BufferedReader(fr);
             BufferedWriter bfw = new BufferedWriter(fw)) {
            String line;
            while ((line = bfr.readLine()) != null) {
                //每次读取一行、写入一行
                bfw.write(line);
                bfw.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRandom() {
        try (RandomAccessFile in = new RandomAccessFile("D:\\file01.txt", "rw");
             RandomAccessFile out = new RandomAccessFile("D:\\file01_copy3.txt", "rw")) {
            byte[] buf = new byte[1024];
            int hasRead;
            while ((hasRead = in.read(buf)) > 0) {
                //每次读取多少就写多少
                out.write(buf, 0, hasRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
