package com.melek;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FindTestReports {

    private FindTestReports() {
        throw new UnsupportedOperationException("Utility class");
    }

    protected static List<Path> getJsonFiles() throws IOException {
        String reportDirectoryPath = "target/test-report";
        try (Stream<Path> paths = Files.walk(Paths.get(reportDirectoryPath))) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".json"))
                    .toList();
        }
    }
}
