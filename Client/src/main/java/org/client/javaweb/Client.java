package org.client.javaweb;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private String host;
    private int port;
    private Logger logger = LoggerFactory.getLogger(Client.class);

    public Client(String h, int p){
        this.host = h;
        this.port = p;
    }

    public void connect(){
        try {
            InetAddress address = InetAddress.getByName(host);
            socket = new Socket(address, port);
            if (!socket.isClosed()){
                logger.info("Connected with client");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(){
        try {
            PrintWriter writerClient = new PrintWriter(socket.getOutputStream());
            BufferedReader readerClient = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(),
                            StandardCharsets.UTF_8));
            String text, textW;
            Scanner scan = new Scanner(System.in);

            while(true){
                text = readerClient.readLine();
                System.out.println(text);
                textW = scan.nextLine();
                if(textW.equals("e")){
                    writerClient.println(textW);
                    writerClient.flush();
                    break;
                } else {
                    writerClient.println(textW);
                    writerClient.flush();
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(){
        if (!socket.isClosed()){
            try {
                socket.close();
                logger.info("Finished connection");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
