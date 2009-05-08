package de.ama.framework.mail;

import de.ama.framework.action.ActionScriptAction;
import de.ama.util.Environment;
import de.ama.util.Ini;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ama
 * Date: 08.02.2005
 * Time: 22:30:56
 * To change this template use File | Settings | File Templates.
 */
public class ReceiveMailsServerAction extends ActionScriptAction {

    public void executeOnServer() throws MessagingException, IOException {
        Map map = new HashMap();
        File filesDir = Environment.getHomeRelativDir("files");
        map.put("*", Ini.getString("mail.attachment.dir", filesDir.getAbsolutePath()  , "temporäres Verzeichnis für eingehende mail-attachements"));

        data = MailFacade.receiveEmails("dbhcom01.dbh.de", -1, "ama", ".ama", map, false, 100);

    }
}
