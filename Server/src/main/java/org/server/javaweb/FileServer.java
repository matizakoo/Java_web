package org.server.javaweb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class FileServer {
    private ServerSocket serverSocket;
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private String defaultLocation;
    private Logger logger = LoggerFactory.getLogger(FileServer.class);

    public FileServer(int port){
        try {
            serverSocket = new ServerSocket(port);
            defaultLocation = "Server/filesserver/";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serverConnection() {
        System.out.println("korzystam z adresu" + serverSocket.getInetAddress().getHostAddress());
        while (true) {
            logger.info("Waiting for client");
            try {
                socket = serverSocket.accept();
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                logger.info("Connected with: " + socket.getInetAddress().getHostAddress());
                getFileFromClient();
//                Thread.sleep(500);
                sendFileToClient();
                clientClose();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendFileToClient(){
        File file = new File( defaultLocation + "text.txt");
        //
        logger.info("Preparing to send data");
        byte[] fileNameBytes = file.getName().getBytes(StandardCharsets.UTF_8);
        int fileNameLength = file.getName().length();
        //
        try {
            FileInputStream fileIn = new FileInputStream(file);
            byte[] fileContentBytes = new byte[(int) file.length()];
            fileIn.read(fileContentBytes);
            fileIn.close();
            // wysyłamyy nazwę pliku
            dataOutputStream.writeInt(fileNameLength);
            dataOutputStream.write(fileNameBytes, 0, fileNameLength);
            // wysyłanie zawartości
            dataOutputStream.writeLong(file.length());
            dataOutputStream.write(fileContentBytes);
            dataOutputStream.flush();
            logger.info("File has been sent");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getFileFromClient(){
        try {
            int fileNameLength = dataInputStream.readInt();
            // sprawdzam czy przesłano nazwę pliku
            if (fileNameLength > 0){
                byte[] fileNameBytes = new byte[fileNameLength];
                dataInputStream.readFully(fileNameBytes);
                String fileName = new String(fileNameBytes,
                        0,
                        fileNameLength,
                        StandardCharsets.UTF_8);
                long fileContentLength = dataInputStream.readLong();
                // Checking if file is empty
                if (fileContentLength > 0){
                    byte[] fileContentBytes = new byte[(int) fileContentLength];
                    dataInputStream.readFully(fileContentBytes);
                    FileOutputStream fileOut = new FileOutputStream(
                            (defaultLocation + fileName));
                    fileOut.write(fileContentBytes);
                    fileOut.flush();
                    fileOut.close();
                    logger.info("New file has been saved " + fileName);
                }else{
                    logger.info("Creating new empty file");
                }
            }else{
                logger.info("File hasn't been transfered");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clientClose(){
        if(!socket.isClosed()){
            try {
                dataOutputStream.close();
                dataInputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
