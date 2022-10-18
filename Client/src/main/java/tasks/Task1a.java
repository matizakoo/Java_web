package tasks;

import org.client.javaweb.PackageZIP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

public class Task1a {
    Logger logger = LoggerFactory.getLogger(Task1a.class);
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
