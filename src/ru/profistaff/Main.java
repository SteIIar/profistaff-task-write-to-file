package ru.profistaff;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {


    private static List<Path> fileList = new ArrayList<>();
    private static Path rootPath;

    public static void main(String[] args) {
        rootPath = Paths.get(args[0]);

        try {
            collectFileList(rootPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        collectFilesToOne(Paths.get(args[1]));

    }

    public static void collectFilesToOne(Path path) {
        try {
            if (Files.deleteIfExists(path))
                Files.createFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }


        fileList.stream()
                .sorted(Comparator.comparing(Path::getFileName))
                .forEach(p -> {
                    System.out.println(p.getFileName());
                    byte[] bytes;
                    try {
                        bytes = Files.readAllBytes(p);
                        Files.write(path, bytes, StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    public static void collectFileList(Path path) throws Exception {
        if (Files.isRegularFile(path) && path.toString().endsWith(".txt")) {
           fileList.add(path);
        }

        if (Files.isDirectory(path)) {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
                for (Path file : directoryStream) {
                    collectFileList(file);
                }
            }
        }
    }
}
