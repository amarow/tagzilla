package de.ama.server.actions;

import de.ama.util.Util;

import java.io.File;


public class StartExecAction extends ServerAction {
    public String cmdline=null;
    public boolean  waitUntilReady;

    public void execute() {
        try {
            String[] command = cmdline.split(" ");
            Runtime runTime = Runtime.getRuntime();
            if (waitUntilReady) {
                runTime.exec(command).waitFor();
            } else {
                runTime.exec(command);
            }
        } catch (Exception e) {
            addError(e.getMessage());
            detailErrorMessage = Util.getAllExceptionInfos(e);
            System.out.println(detailErrorMessage);
        }
    }

    public boolean needsUser() {
        return false;
    }

}