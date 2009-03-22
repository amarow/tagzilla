package de.ama.server.actions;

import de.ama.server.services.Environment;
import de.ama.server.bom.Handle;
import de.ama.util.Util;
import de.ama.util.UniversalIterator;

import java.util.List;


public class TagAction extends ServerAction {
    public String tag;

    public void execute() {
        if (!Util.isEmpty(tag)) {

            List handles = (List) new UniversalIterator((Object[]) data).asList();
            for (int i = 0; i < handles.size(); i++) {
                Handle handle = (Handle) handles.get(i);
                String oidstr = Environment.getPersistentService().getOidString(handle);
                handle = (Handle) Environment.getPersistentService().getObject(oidstr);
                handle.addTag(tag);
            }
            commit();
        }

    }

    public boolean needsUser() {
        return false;
    }
}