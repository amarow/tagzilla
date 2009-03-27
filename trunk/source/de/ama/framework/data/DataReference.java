package de.ama.framework.data;

/**
 * Created by IntelliJ IDEA.
 * User: ama
 * Date: 25.11.2003
 * Time: 20:33:59
 * To change this template use Options | File Templates.
 */
public class DataReference extends DataProxy{

    public DataReference(Class c, String oidString, String guiRepresentation) {
        super(c, oidString, guiRepresentation);
    }

    public DataReference(Class c) {
        super(c);
    }

    public DataReference(Data data) {
        super(data);
    }

    public DataReference(DataProxy dataProxy) {
        super(dataProxy.getType(), dataProxy.getOidString() , dataProxy.getGuiRepresentation());
        guiRepresentationLong = dataProxy.guiRepresentationLong;
    }

    public boolean isReference(){
        return true;
    }

}
