package org.client.javaweb;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        //-----MESSAGES BETWEEN CLIENT AND SERVER-----

        //        Client c = new Client("localhost", 5501);
//        c.connect();
//        c.sendMessage();
//        c.disconnect();


//        FileClient fc = new FileClient("localhost", 5501, "Client/files/");
//        fc.connect();
//        fc.sendFileToServer();
//        fc.getFileFromServer();
//        fc.disconnect();

        //-----ARCHIEVING AND UNARCHIVING FIELS-----

//        PackageZIP packageZIP = new PackageZIP();
//        File[] files = Paths.get("Client//malpki").toFile().listFiles();
//        packageZIP.packageArchive(files, "archieve.zip");
//        packageZIP.unpackageArchive(Path.of("Client//filesOut"), "archieve.zip");


        //-----TASK 1------
        Task1 task1 = new Task1();
        Scanner in = new Scanner(System.in);
        task1.a(in.next());
        in.close();


    }
}