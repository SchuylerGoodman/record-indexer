/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.database.tools;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 *
 * @author schuyler
 */
public class FileDeleter extends SimpleFileVisitor<Path> {

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
            throws IOException {
        String f = file.toString();
        Files.delete(file);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        if (exc == null) {
            String d = dir.toString();
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
        }
        else {
            throw exc;
        }
    }
    
}
