package uj.wmii.pwj.gvt;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandHandler {

    private final Map<String, Command> commands;
    private final ExitHandler exitHandler;
    private final VersionManager versionManager;

    public CommandHandler(ExitHandler exitHandler,  VersionManager versionManager) {
        this.exitHandler = exitHandler;
        this.versionManager = versionManager;
        commands = new HashMap<>();
        addCommandsToMap();
    }

    public void handle(String[] args){
        String commandName = args[0];
        if (!commands.containsKey(commandName)){
            exitHandler.exit(1, "Unknown command " +  commandName);
        } else {
            Command command = commands.get(commandName);
            command.execute(this.exitHandler, args);
        }
    }

    private void addCommandsToMap(){
        this.commands.put("init", new InitCommand(this.versionManager));
        this.commands.put("add", new AddCommand(this.versionManager));
        this.commands.put("detach", new DetachCommand(this.versionManager));
        this.commands.put("checkout", new CheckoutCommand(this.versionManager));
        this.commands.put("commit", new CommitCommand(this.versionManager));
        this.commands.put("history", new HistoryCommand(this.versionManager));
        this.commands.put("version", new VersionCommand(this.versionManager));
    }
}
