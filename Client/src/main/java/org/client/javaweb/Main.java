package org.client.javaweb;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        //        Client c = new Client("localhost", 5501);
//        c.connect();
//        c.sendMessage();
//        c.disconnect();


//        FileClient fc = new FileClient("localhost", 5501, "Client/files/");
//        fc.connect();
//        fc.sendFileToServer();
//        fc.getFileFromServer();
//        fc.disconnect();

        PackageZIP packageZIP = new PackageZIP();
        File[] files = Paths.get("Client//malpki").toFile().listFiles();
//        packageZIP.packageArchive(files, "archieve.zip");
        packageZIP.unpackageArchive(Path.of("Client//filesOut"), "archieve.zip");
    }
}