package uj.wmii.pwj.gvt;

import java.io.IOException;

public class HistoryCommand implements Command{
    private final VersionManager versionManager;

    public HistoryCommand(VersionManager versionManager) {
        this.versionManager = versionManager;
    }

    @Override
    public void execute(ExitHandler exitHandler, String[] args) {
        StringBuilder sb = new StringBuilder();
        try {
            //int n = args.length < 3 || !args[1].equals("-last") ? 0 : Integer.parseInt(args[2]);
            int latestVersionNumber = versionManager.getLatestVersionNumber();
            int startVersion = 0;
            if(args.length > 2 && args[1].equals("-last")){
                try{
                    int count = Integer.parseInt(args[2]);
                    startVersion = Math.max(0, latestVersionNumber-count+1);
                } catch(NumberFormatException e){}
            }
            for (int i = latestVersionNumber; i >= startVersion; i--) {
                String message = versionManager.getVersionMessage(i);
                message = message.split("\n")[0];
                String a = i + ": " + message + "\n";
                sb.append(a);
            }
            exitHandler.exit(0, sb.toString());
        } catch (IOException e){
            e.printStackTrace(System.err);
            exitHandler.exit(-3, "Underlying system problem. See ERR for details.");
        }
    }
}
