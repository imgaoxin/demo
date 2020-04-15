package org.test.demo.base.io;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author gx
 * @create 2019-09-16 14:47
 */
public class BIOTest {
    public static void main(String[] args) {
        new Thread(() -> {
            try {
                server();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                client();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    static void server() throws IOException {
        final ServerSocket serverSocket = new ServerSocket(9222);
        // while(true){
        final Socket socket = serverSocket.accept();
        final InputStream inputStream = socket.getInputStream();
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        final OutputStream outputStream = socket.getOutputStream();
        final PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream));
        while (true) {
            final String s = bufferedReader.readLine();
            if (s == null) {
                break;
            }
            printWriter.println("receive:" + s);
            printWriter.flush();
        }

        printWriter.close();
        outputStream.close();
        bufferedReader.close();
        inputStream.close();
        socket.close();
        // }
        serverSocket.close();
    }

    static void client() throws IOException {
        final Socket socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", 9222));
        final InputStream inputStream = socket.getInputStream();
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        final OutputStream outputStream = socket.getOutputStream();
        final PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream), true);

        printWriter.println("hello");
        System.out.println(bufferedReader.readLine());
        printWriter.println("exit");
        System.out.println(bufferedReader.readLine());

        printWriter.close();
        outputStream.close();
        bufferedReader.close();
        inputStream.close();
        socket.close();
    }
}
