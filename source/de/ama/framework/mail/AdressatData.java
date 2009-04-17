package de.ama.framework.mail;

import de.ama.framework.data.Data;
import de.ama.util.Util;

/**
 * Created by IntelliJ IDEA.
 * User: ama
 * Date: 08.02.2005
 * Time: 21:29:07
 * To change this template use File | Settings | File Templates.
 */
public class AdressatData extends Data{
    public  String adresse;
    public  String name;
    public  int    typ;

    public String getGuiRepresentation() {
        return Util.saveToString(name);
    }

}
