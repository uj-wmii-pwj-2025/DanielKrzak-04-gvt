package uj.wmii.pwj.gvt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DetachCommand implements Command{
    private final VersionManager versionManager;

    public DetachCommand(VersionManager versionManager) {
        this.versionManager = versionManager;
    }

    private String getMessage(String[] args){
        return args[args.length-2].equals("-m") ? args[args.length-1] : "File detached successfully. File: " + args[1];
    }

    @Override
    public void execute(ExitHandler exitHandler, String[] args) {
        if(args.length < 2) {
            exitHandler.exit(30, "Please specify file to detach.");
            return;
        }
        String filename = args[1];
        String message = getMessage(args);
        try {
            if(!versionManager.fileExistsInLatestVersion(filename)) {
                exitHandler.exit(0, "File is not added to gvt. File: " + filename);
                return;
            }
            Path newVersionPath = versionManager.prepareNewVersion();
            Files.deleteIfExists(newVersionPath.resolve("content").resolve(filename));
            versionManager.finalizeNewVersion(newVersionPath, message);
            exitHandler.exit(0, "File detached successfully. File: " + filename);
        } catch (IOException e){
            e.printStackTrace(System.err);
            exitHandler.exit(31, "File cannot be detached, see ERR for details. File: " + filename);
        }
    }
}
