package uj.wmii.pwj.gvt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class CommitCommand implements Command{
    private final VersionManager versionManager;

    public CommitCommand(VersionManager versionManager) {
        this.versionManager = versionManager;
    }

    private String getMessage(String[] args){
        return args[args.length-2].equals("-m") ? args[args.length-1] : "File committed successfully. File: " + args[1];
    }

    @Override
    public void execute(ExitHandler exitHandler, String[] args) {
        if(args.length == 1) {
            exitHandler.exit(50, "Please specify file to commit.");
            return;
        }
        String filename = args[1];
        Path sourceFile = Paths.get(filename);
        String message = getMessage(args);
        try {
            if(!Files.exists(sourceFile)) {
                exitHandler.exit(51, "File not found. File: " +  filename);
            }
            if(!versionManager.fileExistsInLatestVersion(filename)) {
                exitHandler.exit(0, "File is not added to gvt. File: " + filename);
                return;
            }
            Path newVersionPath = versionManager.prepareNewVersion();
            Files.copy(sourceFile, newVersionPath.resolve("content").resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            versionManager.finalizeNewVersion(newVersionPath, message);
            exitHandler.exit(0, "File committed successfully. File: " + filename);
        } catch (IOException e){
            e.printStackTrace(System.err);
            exitHandler.exit(52, "File cannot be committed, see ERR for details. File: " + filename);
        }
    }
}
