package uj.wmii.pwj.gvt;
import java.io.File;
import java.io.IOException;

public class InitCommand implements Command{
    private final VersionManager versionManager;

    public InitCommand(VersionManager versionManager) {
        this.versionManager = versionManager;
    }

    @Override
    public void execute(ExitHandler exitHandler, String[] args) {
        prepareGvtDirectory(exitHandler);
        prepareVersionsDirectory(exitHandler);
        try{
            versionManager.makeFirstVersion();
        } catch (IOException e) {
            e.printStackTrace(System.err);
            exitHandler.exit(-3, "Underlying system problem. See ERR for details.");
        }
        exitHandler.exit(0, "Current directory initialized successfully.");
    }

    public void prepareGvtDirectory(ExitHandler exitHandler){
        File gvtDir = new File(".gvt");
        if(gvtDir.exists()) exitHandler.exit(10, "Current directory is already initialized.");
        if(!gvtDir.mkdir()) {
            new Exception().printStackTrace(System.err);
            exitHandler.exit(-3, "Underlying system problem. See ERR for details.");
        }
    }

    public void prepareVersionsDirectory(ExitHandler exitHandler){
        File versionsDir = new File(".gvt\\versions");
        if(!versionsDir.mkdir()) {
            new Exception().printStackTrace(System.err);
            exitHandler.exit(-3, "Underlying system problem. See ERR for details.");
        }
    }
}
