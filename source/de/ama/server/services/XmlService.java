
package de.ama.server.services;

/**
 * User: x
 * Date: 26.10.2008
 */
public interface XmlService {
    public static String NAME = "XmlService";
    public Object     toObject   (String xml);
    public String     toXmlString(Object obj);
}
