package org.server.javaweb;

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

    public FileServer(int port){
        try {
            this.serverSocket = new ServerSocket(port);
            this.defaultLocation = "Server/filesserver/";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serverConnection() {
        System.out.println("korzystam z adresu" + serverSocket.getInetAddress().getHostAddress());
        while (true) {
            System.out.println("Oczekuje na klienta");
            try {
                this.socket = serverSocket.accept();
                this.dataInputStream = new DataInputStream(socket.getInputStream());
                this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
                System.out.println("Połączenie nawiązane z adresu " + socket.getInetAddress().getHostAddress());
                getFileFromClient();
//                Thread.sleep(500);
                sendFileToClient();
                clientClose();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }

    public void sendFileToClient(){
        File file = new File( defaultLocation + "text.txt");
        //
        System.out.println("Przygotowania do wysłania pliku");
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
            System.out.println("Plik został wysłany");
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
                // sprawdzam czy plik ma jakąś zawartość
                if (fileContentLength > 0){
                    byte[] fileContentBytes = new byte[(int) fileContentLength];
                    dataInputStream.readFully(fileContentBytes);
                    FileOutputStream fileOut = new FileOutputStream(
                            new File(defaultLocation + fileName));
                    fileOut.write(fileContentBytes);
                    fileOut.flush();
                    fileOut.close();
                    System.out.println("Zapisano nowy plik o nazwie " + fileName);
                }else{
                    System.out.println("Utworzę pusty plik");
                }
            }else{
                System.out.println("nie przekazano pliku");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clientClose(){
        if(!this.socket.isClosed()){
            try {
                this.dataOutputStream.close();
                this.dataInputStream.close();
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
