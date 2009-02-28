package server.services.impl;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import server.services.XmlService;


public class XmlServiceImpl implements XmlService {

    XStream xstream = new XStream(new DomDriver());

    public XmlServiceImpl() {
    }

    public Object toObject(String xml){
        try {
            return readBOFromXML(xml);
        } catch (Exception e) {
            throw new RuntimeException("toObject failed",e);
        }
    }

    public String toXmlString(Object obj){
        try {
            return readXMLFromBO(obj);
        } catch (Exception e) {
            throw new RuntimeException("toXmlString failed",e);
        }
    }


    private String readXMLFromBO(Object o) {
        return xstream.toXML(o);
    }

    private Object readBOFromXML(String xml) {
        return xstream.fromXML(xml);
    }

}