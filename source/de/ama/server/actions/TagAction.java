package de.ama.server.actions;

import de.ama.server.bom.Handle;
import de.ama.server.bom.HandleData;
import de.ama.server.services.Environment;
import de.ama.util.UniversalIterator;
import de.ama.util.Util;

import java.util.List;


public class TagAction extends ServerAction {
    public String tag;

    public void execute() {
        if (!Util.isEmpty(tag)) {

            List handles = (List) new UniversalIterator((Object[]) data).asList();
            for (int i = 0; i < handles.size(); i++) {
                HandleData data = (HandleData) handles.get(i);
                Handle handle = (Handle) Environment.getPersistentService().getObject(data.getOidString());
                handle.addTag(tag);
            }
            commit();
        }

    }

    public boolean needsUser() {
        return false;
    }
}