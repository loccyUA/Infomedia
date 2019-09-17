package com.infomedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Application {

    //  call in terminal format: >mvn -q clean compile exec:java -Dexec.mainClass="com.infomedia.Application" -Dexec.args="'%path' %fileSizeInGB %DisplayLimit"
    // call example: >mvn -q clean compile exec:java -Dexec.mainClass="com.infomedia.Application" -Dexec.args="'C:\Program Files' 1 2"
    public static void main(String... args) {
        if (args.length < 3) {
            throw new IllegalArgumentException("Please provide ");
        }
        File folder = new File(args[0]);
        long fileSize = Integer.parseInt(args[1]) * 1024 * 1024 * 1024;
        int displaySize = Integer.parseInt(args[2]);

        List<FolderStatistics> bigFolders = new ArrayList<>();
        folderSize(folder, bigFolders, fileSize);
        bigFolders.sort((p1, p2) -> Long.compare(p2.getSize(), p1.getSize()));

        for (int i = 0; i < bigFolders.size(); i++) {
            FolderStatistics statistics = bigFolders.get(i);
            System.out.printf("Size: %f GB, Path: %s\n", statistics.getSize() / (1024D * 1024 * 1024), statistics.getPath());
            if (i >= displaySize) {
                break;
            }
        }
    }

    private static long folderSize(File file, List<FolderStatistics> results, long fileSize) {
        long length = 0;
        if (file == null) {
            return 0L;
        }
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (subFiles == null) {
                return 0L;
            }
            for (File subFile : subFiles) {
                length += folderSize(subFile, results, fileSize);
            }
            if (fileSize < length) {
                results.add(new FolderStatistics(file.getAbsolutePath(), length));
            }
            return length;
        } else {
            return file.length();
        }
    }

    static class FolderStatistics {
        private String path;
        private long size;

        FolderStatistics(String path, long size) {
            this.path = path;
            this.size = size;
        }

        String getPath() {
            return path;
        }

        long getSize() {
            return size;
        }
    }
}