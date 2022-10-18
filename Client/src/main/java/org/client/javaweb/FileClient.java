package org.client.javaweb;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileClient {
    private Socket socket;
    private String host;
    private int port;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private String defaultLocation;
    private Logger logger = LoggerFactory.getLogger(FileClient.class);

    public FileClient(String host, int port, String defaultLocation){
        this.host = host;
        this.port = port;
        this.defaultLocation = defaultLocation;
    }

    public void connect(){
        try {
            InetAddress address = InetAddress.getByName(host);
            socket = new Socket(address, port);
            if (socket.isConnected()){
                logger.info("Connected with server");
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFileToServer(){
        File file = new File("Client/files/file.txt");
        //
        logger.info("Preparing files to send data");
        byte[] fileNameBytes = file.getName().getBytes(StandardCharsets.UTF_8);
        int fileNameLength = file.getName().length();
        //
        try {
            FileInputStream fileIn = new FileInputStream(file);
            byte[] fileContentBytes = new byte[(int) file.length()];
            fileIn.read(fileContentBytes);
            fileIn.close();
            // sending file name
            dataOutputStream.writeInt(fileNameLength);
            dataOutputStream.write(fileNameBytes, 0, fileNameLength);
            // sending data
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

    public void getFileFromServer(){
        try {
            int fileNameLength = dataInputStream.readInt();
            // Checking if file name is corret
            if (fileNameLength > 0){
                byte[] fileNameBytes = new byte[fileNameLength];
                dataInputStream.readFully(fileNameBytes);
                String fileName = new String(fileNameBytes,
                        0,
                        fileNameLength,
                        StandardCharsets.UTF_8);
                long fileContentLength = dataInputStream.readLong();
                // Checking if file is not empty
                if (fileContentLength > 0){
                    byte[] fileContentBytes = new byte[(int) fileContentLength];
                    dataInputStream.readFully(fileContentBytes);
                    FileOutputStream fileOut = new FileOutputStream(
                            defaultLocation + fileName);
                    fileOut.write(fileContentBytes);
                    fileOut.flush();
                    fileOut.close();
                    logger.info("Saving file:  " + fileName);
                }else{
                    logger.info("Creating empty file");
                }
            }else{
                logger.info("File hasn't been sent");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(){
        if (!socket.isClosed()){
            try {
                dataInputStream.close();
                dataOutputStream.close();
                socket.close();
                logger.info("Finished connection");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void getFileFromServerX(){
        Scanner scanner1 = new Scanner(System.in);
        String s1 = scanner1.nextLine();
        Path path1 = Path.of("Server//filesserver" + s1);
        File file1 = new File(path1.toUri());
        PackageZIP packageZIP = new PackageZIP();
        if (file1.exists()){
            packageZIP.unpackageArchive(path1, "noweArchiwum.zip");
            // tworzenie nowego katalogu do rozpakowania
        } else {
            try {
                packageZIP.unpackageArchive(Files.createDirectory(Paths.get(path1.toUri())), "noweArchiwum.zip");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
