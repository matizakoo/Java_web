package org.server.javaweb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    private Logger logger = LoggerFactory.getLogger(Server.class);

    public Server(int port){
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serverConnection(){
        while(true){
            logger.info("Waiting for client");
            try {
                socket = serverSocket.accept();
                logger.info("Connected");
                serverMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void serverMessage(){
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    socket.getInputStream(), StandardCharsets.UTF_8
            ));

            String text;
            writer.println("Hello. Who are you?");
            while(true){
                text = reader.readLine();
                if (text.equals("e")){
                    System.out.println("klient kończy połączenie");
                    logger.info("Client has disconnected");
                    writer.close();
                    reader.close();
                    break;
                }
                System.out.println(text);
                writer.println("Re: " + text);
            }
            clientClose();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clientClose(){
        if(!socket.isClosed()){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
