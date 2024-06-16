package com.lumatest.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class DeleteFileUtils {

    public static void deleteFile(String path, String fileName) {
        try {
            File directory = new File(path);
            File[] files = directory.listFiles((dir, name) -> name.endsWith(fileName));
            if (files != null) {
                for (File file : files) {
                    if (!file.delete()) {
                        System.out.println("Failed to delete file: " + file.getAbsolutePath());
                        Files.delete(file.toPath());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
