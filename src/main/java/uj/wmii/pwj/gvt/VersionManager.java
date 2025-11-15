package uj.wmii.pwj.gvt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VersionManager {

    private final VersionCreator versionCreator;
    private final Path versionsPath;

    public VersionManager() {
        this.versionCreator = new VersionCreator();
        versionsPath = Paths.get(".gvt", "versions");
    }


    private void updateInfo(int latest, int active) throws IOException {
        Path infoFilePath = Paths.get(".gvt").resolve("info.txt");
        String content = "Latest: " + Integer.toString(latest) + "\nActive: " + Integer.toString(active);
        Files.deleteIfExists(infoFilePath);
        Files.write(infoFilePath, content.getBytes());
    }

    private int[] getInfo() throws IOException {
        Path infoFilePath = Paths.get(".gvt").resolve("info.txt");
        List<String> lines = Files.readAllLines(infoFilePath);
        int[] result = new int[2];
        for(int i = 0; i < 2; i++){
            result[i] = Integer.parseInt(lines.get(i).trim().split(" ")[1]);
        }
        return result;
    }

    private List<Path> getVersionsFiles(int versionNumber) throws IOException {
        Path versionContentPath = this.versionsPath.resolve(Integer.toString(versionNumber)).resolve("content");
        try (Stream<Path> files = Files.list(versionContentPath)) {
            return files.collect(Collectors.toList());
        }
    }

    public String getVersionMessage(int versionNumber) throws IOException {
        Path metaFilePath = this.versionsPath.resolve(Integer.toString(versionNumber)).resolve("_metadata.txt");
        List<String> lines = Files.readAllLines(metaFilePath);
        StringBuilder sb = new StringBuilder();
        for(String line : lines){
            sb.append(line).append("\n");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public Version getVersion(int versionNumber) throws IOException {
        return new Version(versionNumber, getVersionMessage(versionNumber), getVersionsFiles(versionNumber));
    }

    public void makeFirstVersion() throws  IOException {
        versionCreator.createFirstVersion();
        updateInfo(0, 0);
    }

    public boolean fileExistsInLatestVersion(String filename) throws IOException {
        Path latestContentPath = this.versionsPath.resolve(Integer.toString(getInfo()[0])).resolve("content");
        Path filePath = latestContentPath.resolve(filename);
        return Files.exists(filePath);
    }

    public Path prepareNewVersion() throws IOException {
        return versionCreator.prepareNewVersion(getVersion(getInfo()[0]));
    }

    public void finalizeNewVersion(Path versionPath, String message) throws IOException {
        versionCreator.finalizeNewVersion(versionPath, message);
        int newNumber = getInfo()[0]+1;
        updateInfo(newNumber, newNumber);
    }

    public int getLatestVersionNumber() throws IOException {
        return getInfo()[0];
    }

    public int getActiveVersionNumber() throws IOException {
        return getInfo()[1];
    }

    public void setActiveVersionNumber(int versionNumber) throws IOException {
        updateInfo(getInfo()[0], versionNumber);
    }

    public boolean versionExists(int versionNumber) throws IOException {
        return versionNumber <= getInfo()[0] && versionNumber >= 0;
    }
}
