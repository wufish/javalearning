package com.wufish.javalearning.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

/**
 * @author wuzhijun05 <wuzhijun05@kuaishou.com>
 * Created on 2023-10-08
 */
public class FileNIOTester {
    @Test
    public void testNIO() {
        try (FileInputStream fis = new FileInputStream("D:\\file01.txt");
             FileOutputStream fos = new FileOutputStream("D:\\file01_copy4.txt");
             FileChannel inc = fis.getChannel();
             FileChannel outc = fos.getChannel()
        ) {
            ByteBuffer buffer = ByteBuffer.allocate(4);
            //多次重复"取水"的方式
            while (inc.read(buffer) != -1) {
                buffer.flip();
                outc.write(buffer);
                buffer.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNIOMap() {
        File f = new File("D:\\file01.txt");
        try (FileInputStream fis = new FileInputStream(f);
             FileOutputStream fos = new FileOutputStream("D:\\file01_copy5.txt");
             FileChannel inc = fis.getChannel();
             FileChannel outc = fos.getChannel()
        ) {
            //将 FileChannel 里的全部数据映射到 ByteBuffer 中
            MappedByteBuffer mappedByteBuffer = inc.map(FileChannel.MapMode.READ_ONLY, 0, f.length());
            outc.write(mappedByteBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNIOTransferFrom() {
        try (FileInputStream fis = new FileInputStream("D:\\file01.txt");
             FileOutputStream fos = new FileOutputStream("D:\\file01_copy06.txt");
             FileChannel srcChannel = fis.getChannel();
             FileChannel destChannel = fos.getChannel();
        ) {
            destChannel.transferFrom(srcChannel, 0, srcChannel.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNIOTransferTo() {
        try (FileInputStream fis = new FileInputStream("D:\\file01.txt");
             FileOutputStream fos = new FileOutputStream("D:\\file01_copy06.txt");
             FileChannel srcChannel = fis.getChannel();
             FileChannel destChannel = fos.getChannel();
        ) {
            long size = srcChannel.size();
            long position = 0;
            while (size > 0) {
                long count = srcChannel.transferTo(position, srcChannel.size(), destChannel);
                position += count;
                size -= count;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void copy() {
        try (OutputStream fos = new FileOutputStream("D:\\file01_copy6.txt")) {
            Files.copy(Paths.get("D:\\file01.txt"), fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
