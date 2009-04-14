package de.ama.framework.mail;

import de.ama.db.PersistentMarker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ama
 * Date: 08.02.2005
 * Time: 20:24:05
 * To change this template use File | Settings | File Templates.
 */
public class Email implements PersistentMarker{

    private String message_TEXT;
    private String subject;
    private String fromName;
    private String fromAddress;
    private String replyToName;
    private String replyToAddress;
    private Date   date;
    private int    msgNum;

    private List  toAdressats = new ArrayList();
    private List  attachments = new ArrayList();

    public String getMessage() {
        return message_TEXT;
    }

    public void setMessage(String message) {
        this.message_TEXT = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getReplyToName() {
        return replyToName;
    }

    public void setReplyToName(String replyToName) {
        this.replyToName = replyToName;
    }

    public String getReplyToAddress() {
        return replyToAddress;
    }

    public void setReplyToAddress(String replyToAddress) {
        this.replyToAddress = replyToAddress;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void addTo(Adressat adressat) {
        if(toAdressats.contains(adressat)){
            return ;
        }
        toAdressats.add(adressat);
    }


    public void setReplyTo(Adressat adressat) {
        replyToAddress=adressat.getAdresse();
        replyToName=adressat.getName();
    }

    public Adressat getReplyTo() {
        if(replyToAddress==null){
            return null;

        }
        return new Adressat(replyToAddress, replyToName, Adressat.EMAIL);
    }


    public void setFrom(Adressat adressat) {
        fromAddress=adressat.getAdresse();
        fromName=adressat.getName();
    }

    public Adressat getFrom() {
        if(fromAddress==null){
            return null;
        }
        return new Adressat(fromAddress, fromName, Adressat.EMAIL);
    }



    public List getAttachments() {
        return attachments;
    }

    public void setAttachments(List as){
       attachments=as;
    }


    public void addAttachment(String  atmnt){
       attachments.add(atmnt);
    }

    public List getToAdressats() {
        return toAdressats;
    }

    public int getMsgNum() {
        return msgNum;
    }

    public void setMsgNum(int msgNum) {
        this.msgNum = msgNum;
    }

}
