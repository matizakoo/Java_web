package org.client.javaweb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Task1 {
    Logger logger = LoggerFactory.getLogger(Task1.class);
    //a
    public void a(String path) throws FileNotFoundException {
        PackageZIP packageZIP = new PackageZIP();
        File file = Paths.get(path).toFile();
        if (file.exists()) {
            packageZIP.packageArchiveFile(file);
        } else {
            packageZIP.packageArchiveFile(new File("Client//malpki"));
        }
    }
}
