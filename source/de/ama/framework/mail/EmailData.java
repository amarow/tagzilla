package de.ama.framework.mail;

import de.ama.framework.data.Data;
import de.ama.framework.data.DataTable;
import de.ama.framework.data.StringData;
import de.ama.util.StringDivider;
import de.ama.util.Util;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ama
 * Date: 08.02.2005
 * Time: 21:27:53
 * To change this template use File | Settings | File Templates.
 */
public class EmailData extends Data{
    public String message;
    public String subject;
    public String fromName;
    public String fromAddress;
    public String replyToName;
    public String replyToAddress;
    public Date   date;
    public int    msgNum;
    public DataTable attachmentsString= new DataTable();
    public DataTable   toAdressats  = new DataTable();

    public String[] getTableColKeys() {
        return new String[]{"msgNum","fromName","date","subject","attachmentsString"};
    }

    public void createQueryDescriptions() {
        addQueryDescription("msgNum");
        addQueryDescription("message");
        addQueryDescription("subject");
        addQueryDescription("fromName");
        addQueryDescription("fromAddress");
        addQueryDescription("attachmentsString","attachments",String.class);
    }

    public String getGuiRepresentation() {
        return Util.saveToString(subject);
    }


    public String getClearText() {
        StringDivider sd = new StringDivider(Util.saveToString(message),"<br>");
        if(sd.ok()){
            return sd.pre();
        }
        return Util.saveToString(message);
    }
}
