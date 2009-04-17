package de.ama.framework.action;

public class StartExecAction extends ActionScriptAction {
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



}