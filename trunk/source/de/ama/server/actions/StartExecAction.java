package de.ama.server.actions;

public class StartExecAction extends ServerAction {
    public String cmdline = null;
    public boolean waitUntilReady;

    public void execute() throws Exception {

        String[] command = cmdline.split(" ");
        Runtime runTime = Runtime.getRuntime();
        if (waitUntilReady) {
            runTime.exec(command).waitFor();
        } else {
            runTime.exec(command);
        }

    }

    public boolean needsUser() {
        return false;
    }

}