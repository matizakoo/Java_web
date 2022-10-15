package org.server.javaweb;

public class Main {
    public static void main(String[] args) {
        //        Server s = new Server(5501);
//        s.serverConnection();
        // uruchominei aplikacji serwerowej do przekazywania plików
        FileServer fs = new FileServer(5501);
        fs.serverConnection();
    }
}