/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.database.tools;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author schuyler
 */
public class FileImporter implements FileVisitor<Path> {
    
    Path target;
    Path currentDirectory;
    
    public FileImporter() {
        target = Paths.get("Files");
        currentDirectory = target;
        try {
            Files.createDirectories(target);
        } catch (IOException ex) {
            Logger.getLogger(FileImporter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        if (dir.toString().equals("Records")) {
            return FileVisitResult.CONTINUE;
        }
        Files.copy(dir, target.resolve(dir.getFileName()));
        currentDirectory = target.resolve(dir.getFileName());
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Files.copy(file, currentDirectory.resolve(file.getFileName()));
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        currentDirectory = currentDirectory.getParent();
        return FileVisitResult.CONTINUE;
    }
    
}
