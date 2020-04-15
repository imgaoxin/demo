package org.test.demo.base.zerocopy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BrokenBarrierException;

/**
 * @author gx
 * @create 2019-08-28 17:06
 */
public class ZeroCopyTest {
    static Thread t1 = null, t2 = null;
    public static void main(String[] args) {
        // file to net
        t1 = new Thread(() -> {
            try {
                accept();
            } catch (IOException | BrokenBarrierException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        t2 = new Thread(() -> {
            try {
                send();
            } catch (IOException | InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();


        // file to file
        /*try {
            //transferToDemo("D:\\workspace\\java\\demo\\src\\main\\resources\\application.yml","D:\\workspace\\java\\spring-boot-scala\\src\\main\\resources\\application1.yml");
            transferFromDemo("D:\\workspace\\java\\demo\\src\\main\\resources\\application.yml","D:\\workspace\\java\\spring-boot-scala\\src\\main\\resources\\application2.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public static void send() throws IOException, BrokenBarrierException, InterruptedException {
        // 发送数据
        SocketChannel sender = SocketChannel.open();

        sender.connect(new InetSocketAddress("localhost", 9026));
        sender.configureBlocking(true);

        FileInputStream fis =  new FileInputStream("D:\\workspace\\java\\demo\\src\\main\\resources\\application.yml");
        FileChannel fileChannel = fis.getChannel();
        long sendSize = fileChannel.transferTo(0, fileChannel.size(), sender);

        System.out.println("size: " + sendSize);
        fileChannel.close();
        fis.close();
        sender.close();
    }

    public static void accept() throws IOException, BrokenBarrierException, InterruptedException {
        //监听端口
        ServerSocketChannel listener = ServerSocketChannel.open();
        ServerSocket serverSocket = listener.socket();
        serverSocket.setReuseAddress(true);
        serverSocket.bind(new InetSocketAddress("localhost", 9026));

        SocketChannel conn = listener.accept();
        conn.configureBlocking(true);

        // 读取数据
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        //int nread = 1;
        //while (nread > 0) {
        //    nread = conn.read(buffer);
        //}
        conn.read(buffer);
        buffer.rewind();

        System.out.println("contents: \n" + new String(buffer.array()));

        buffer.clear();
        conn.close();
        listener.close();
    }


    public static void transferToDemo(String from, String to) throws IOException {

        RandomAccessFile raf_from = new RandomAccessFile(from, "rw");
        RandomAccessFile raf_to = new RandomAccessFile(to, "rw");
        FileChannel fromChannel = raf_from.getChannel();
        FileChannel toChannel = raf_to.getChannel();

        long position = 0;
        long count = fromChannel.size();

        fromChannel.transferTo(position, count, toChannel);

        fromChannel.close();
        toChannel.close();
        raf_to.close();
        raf_from.close();
    }

    @SuppressWarnings("resource")
    public static void transferFromDemo(String from, String to) throws IOException {
        FileChannel fromChannel = new FileInputStream(from).getChannel();
        FileChannel toChannel = new FileOutputStream(to).getChannel();

        long position = 0;
        long count = fromChannel.size();

        toChannel.transferFrom(fromChannel, position, count);

        fromChannel.close();
        toChannel.close();
    }
}
