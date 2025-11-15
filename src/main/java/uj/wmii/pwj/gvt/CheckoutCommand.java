package uj.wmii.pwj.gvt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class CheckoutCommand implements Command{
    private final VersionManager versionManager;

    public CheckoutCommand(VersionManager versionManager) {
        this.versionManager = versionManager;
    }

    @Override
    public void execute(ExitHandler exitHandler, String[] args) {
        if(args.length<2) exitHandler.exit(60, "Invalid version number: ");
        String versionString = args[1];
        try{
            int versionNumber = Integer.parseInt(versionString);
            if (!versionManager.versionExists(versionNumber)) exitHandler.exit(60, "Invalid version number: " + versionNumber);
            Version targetVersion = versionManager.getVersion(versionNumber);
            List<Path> targetFiles = targetVersion.getFiles();
            for(Path targetFilePath : targetFiles){
                Path originalFilePath =  targetFilePath.getFileName();
                Files.copy(targetFilePath, originalFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            versionManager.setActiveVersionNumber(versionNumber);
            exitHandler.exit(0, "Checkout successful for version: " +  versionString);
        } catch (NumberFormatException e){
            exitHandler.exit(60, "Invalid version number: " + versionString);
        } catch (IOException e){
            e.printStackTrace(System.err);
            exitHandler.exit(-3, "Underlying system problem. See ERR for details.");
        }
    }
}
