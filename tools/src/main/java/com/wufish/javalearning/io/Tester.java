package com.wufish.javalearning.io;

import cn.hutool.socket.nio.NioServer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @Author wufish
 * @Create time: 2020/10/7 16:11
 * @Description:
 */
public class Tester {
    public static void main(String[] args) {
    }

    private static void testFileNio() throws IOException {
        //打开一个文件
        RandomAccessFile aFile = new RandomAccessFile("data/nio-data.txt", "rw");
        //获取FileChannel
        FileChannel inChannel = aFile.getChannel();
        //读取数据到ByteBuffer
        ByteBuffer buf = ByteBuffer.allocate(48);
        int bytesRead = inChannel.read(buf);


        //开始写入数据
        //准备数据
        String newData = "New String to write to file";
        ByteBuffer bufWrite = ByteBuffer.allocate(48);
        bufWrite.clear();
        bufWrite.put(newData.getBytes());
        bufWrite.flip();

        FileChannel channel = aFile.getChannel();
        //向文件中写入数据
        while (buf.hasRemaining()) {
            channel.write(buf);
        }

        //关闭FileCHannel
        channel.close();
    }

    private static class TestServer extends NioServer {

        public TestServer(int port) {
            super(port);
        }

        @Override
        protected void read(SocketChannel socketChannel) {

        }

        @Override
        protected void write(SocketChannel socketChannel) {

        }
    }
}
