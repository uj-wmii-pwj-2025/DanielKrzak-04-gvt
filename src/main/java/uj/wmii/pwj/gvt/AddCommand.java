package uj.wmii.pwj.gvt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class AddCommand implements Command{

    private final VersionManager versionManager;

    public AddCommand(VersionManager versionManager) {
        this.versionManager = versionManager;
    }

    private String getMessage(String[] args){
        return args[args.length-2].equals("-m") ? args[args.length-1] : "File added successfully. File: " + args[1];
    }

    @Override
    public void execute(ExitHandler exitHandler, String[] args) {
        if(args.length == 1) exitHandler.exit(20, "Please specify file to add.");
        String filename = args[1];
        Path sourceFile = Paths.get(filename);
        String message = getMessage(args);
        if(!Files.exists(sourceFile)) exitHandler.exit(21, "File not found. File: " +  filename);
        try {
            if(versionManager.fileExistsInLatestVersion(filename)) {
                exitHandler.exit(0, "File already added. File: " + filename);
                return;
            }
            Path newVersionPath = versionManager.prepareNewVersion();
            Files.copy(sourceFile, newVersionPath.resolve("content").resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            versionManager.finalizeNewVersion(newVersionPath, message);
            exitHandler.exit(0, "File added successfully. File: " + filename);
        } catch (IOException e){
            e.printStackTrace(System.err);
            exitHandler.exit(22, "File cannot be added. See ERR for details. File: " + filename);
        }
    }
}
