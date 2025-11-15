package uj.wmii.pwj.gvt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class VersionCreator {

    private Path prepareVersionDir(int versionNumber) throws IOException {
        Path versionPath  = Paths.get(".gvt", "versions", Integer.toString(versionNumber));
        Files.createDirectories(versionPath);
        return versionPath;
    }

    private Path prepareContentDir(Path versionPath) throws IOException {
        Path contentPath = versionPath.resolve("content");
        Files.createDirectories(contentPath);
        return contentPath;
    }

    private void copyAllFiles(Version latestVersion, Path destinationPath) throws IOException {
        List<Path> sourceFileList = latestVersion.getFiles();
        for(Path sourceFile : sourceFileList){
            Path destinationFile = destinationPath.resolve(sourceFile.getFileName());
            Files.copy(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public void createFirstVersion() throws IOException {
        Path versionPath = prepareVersionDir(0);
        prepareContentDir(versionPath);
        Path metaFilePath = versionPath.resolve("_metadata.txt");
        Files.write(metaFilePath, "GVT initialized.".getBytes());
    }

    public Path prepareNewVersion(Version latestVersion) throws IOException {
        Path versionPath = prepareVersionDir(latestVersion.getVersionNumber()+1);
        Path contentPath = prepareContentDir(versionPath);
        copyAllFiles(latestVersion, contentPath);
        return versionPath;
    }

    public void finalizeNewVersion(Path versionPath, String message) throws IOException {
        Path metaFilePath = versionPath.resolve("_metadata.txt");
        Files.write(metaFilePath, message.getBytes());
    }
}