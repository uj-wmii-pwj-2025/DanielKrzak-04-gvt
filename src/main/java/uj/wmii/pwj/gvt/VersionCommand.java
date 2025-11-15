package uj.wmii.pwj.gvt;

import java.io.IOException;

public class VersionCommand implements  Command {
    private final VersionManager versionManager;

    public VersionCommand(VersionManager versionManager) {
        this.versionManager = versionManager;
    }

    @Override
    public void execute(ExitHandler exitHandler, String[] args) {
        try{
            int versionNumber = args.length > 1 ? Integer.parseInt(args[1]) : versionManager.getActiveVersionNumber();
            if(!versionManager.versionExists(versionNumber)) exitHandler.exit(60, "Invalid version number: " +  versionNumber + ".");
            String output = "Version: " + versionNumber + "\n" + versionManager.getVersion(versionNumber).getMessage();
            exitHandler.exit(0, output);
        }
        catch(NumberFormatException e){
            exitHandler.exit(60, "Invalid version number: " + args[1] + ".");
        }
        catch (IOException e){
            e.printStackTrace(System.err);
            exitHandler.exit(-3, "Underlying system problem. See ERR for details.");
        }
    }
}
