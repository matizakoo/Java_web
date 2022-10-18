package org.client.javaweb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class PackageZIP {
    private Logger logger = LoggerFactory.getLogger(PackageZIP.class);
    public void packageArchive(File[] files, String zipName) throws FileNotFoundException {
        try {
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipName));
            zipOut.setLevel(9);
            zipOut.setMethod(ZipOutputStream.DEFLATED);
            if(files.length > 0){
                for(File f: files){
                    ZipEntry zipEntry = new ZipEntry(f.getName());
                    zipOut.putNextEntry(zipEntry);
                    FileInputStream fileIn = new FileInputStream(f);
                    zipOut.write(fileIn.readAllBytes());
                    fileIn.close();
                    zipOut.closeEntry();
                }
                zipOut.close();
                logger.info("Archieved: " + zipName);
            }else{
                logger.info("Files to archive have not been sent");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void packageArchiveFile(File files) throws FileNotFoundException {
        try {
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream("compressedFile.zip"));
            zipOut.setLevel(9);
            zipOut.setMethod(ZipOutputStream.DEFLATED);
            ZipEntry zipEntry = new ZipEntry(files.getName());
            zipOut.putNextEntry(zipEntry);
            FileInputStream fileIn = new FileInputStream(files);
            zipOut.write(fileIn.readAllBytes());
            fileIn.close();
            zipOut.closeEntry();
            zipOut.close();
            logger.info("Archieved: " + files.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void unpackageArchive(Path location, String zipName){
        try{
            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipName));
            ZipEntry zipEntry;
            while((zipEntry = zipIn.getNextEntry()) != null){
                FileOutputStream fileOutputStream = new FileOutputStream(new File(location.toString(), zipEntry.getName()));
                fileOutputStream.write(zipIn.readAllBytes());
                fileOutputStream.flush();
                fileOutputStream.close();
                zipIn.closeEntry();
            }
            zipIn.close();
            logger.info("Unarchieved: " + zipName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
