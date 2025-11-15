package uj.wmii.pwj.gvt;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Gvt {

    private final ExitHandler exitHandler;
    private final CommandHandler commandHandler;
    private final VersionManager versionManager;

    public Gvt(ExitHandler exitHandler) {
        this.exitHandler = exitHandler;
        this.versionManager = new VersionManager();
        this.commandHandler = new CommandHandler(exitHandler, versionManager);
    }

    public static void main(String... args) {
        Gvt gvt = new Gvt(new ExitHandler());
        gvt.mainInternal(args);
    }

    private boolean isInitialized(){
        Path currentDir = Paths.get(".gvt");
        return Files.exists(currentDir) && Files.isDirectory(currentDir);
    }

    public void mainInternal(String... args) {
        if(args.length == 0) {
            exitHandler.exit(1, "Please specify command.");
        }else{
            if(!args[0].equals("init")) {
                if(!isInitialized()) {
                    exitHandler.exit(-2, "Current directory is not initialized. Please use init command to initialize.");
                }
            }
            commandHandler.handle(args);
        }
    }
}
