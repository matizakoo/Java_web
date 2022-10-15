package org.client.javaweb;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class FileClient {
    private Socket socket;
    private String host;
    private int port;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private String defaultLocation;


    public FileClient(String host, int port, String location){
        this.host = host;
        this.port = port;
        this.defaultLocation = location;
    }

    public void connect(){
        try {
            InetAddress address = InetAddress.getByName(host);
            this.socket = new Socket(address, this.port);
            if (socket.isConnected()){
                System.out.println("Udało się nawiązać połączenie ");
                this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
                this.dataInputStream = new DataInputStream(socket.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFileToServer(){
        File file = new File("pliki\\file.txt");
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

    public void getFileFromServer(){
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

    public void disconnect(){
        if (!socket.isClosed()){
            try {
                this.dataInputStream.close();
                this.dataOutputStream.close();
                this.socket.close();
                System.out.println("Połączenie zakończone ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
