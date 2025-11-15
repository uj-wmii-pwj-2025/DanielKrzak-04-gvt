package uj.wmii.pwj.gvt;

public interface Command {
    void execute(ExitHandler exitHandler, String[] args);
}
