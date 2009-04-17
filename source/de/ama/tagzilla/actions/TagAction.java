package de.ama.tagzilla.actions;

import de.ama.tagzilla.data.Handle;
import de.ama.tagzilla.data.HandleData;
import de.ama.server.services.Environment;
import de.ama.util.UniversalIterator;
import de.ama.util.Util;
import de.ama.framework.action.ActionScriptAction;

import java.util.List;


public class TagAction extends ActionScriptAction {
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

}