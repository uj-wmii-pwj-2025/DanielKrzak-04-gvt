package uj.wmii.pwj.gvt;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class Version {

    private final int versionNumber;

    private final String message;

    private final List<Path> files;

    private final Path metaDataPath;

    private final Path path;

    private final Path contentPath;

    public Version(int versionNumber, String message, List<Path> files) {
        this.versionNumber = versionNumber;
        this.message = message;
        this.files = files;
        this.path = Paths.get(".gvt\\versions\\" +  versionNumber);
        this.contentPath = path.resolve("content");
        this.metaDataPath = path.resolve("_metadata.txt");
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public String getMessage() {
        return message;
    }

    public List<Path> getFiles() {
        return Collections.unmodifiableList(files);
    }

    public Path getPath() {
        return path;
    }

    public Path getContentPath() {
        return contentPath;
    }

    public Path metaDataFile() {
        return metaDataPath;
    }
}
