/*
 * DataProxy.java
 *
 * Created on 3. Februar 2003, 22:02
 */

package de.ama.framework.data;

import de.ama.util.Util;
import de.ama.util.XmlElement;

import java.io.Serializable;

/**
 * @author  Andreas Marochow
 * Eine kleine aber sehr wichtige Klasse. Sie sorgt dafür das auf den Client nicht immer die großen
 * Data-Objekte übertragen werden, sondern eben nur eine kleine leichte Proxy-Variante.
 * Ein DataProxy kann Data-Objekte vom RmiServiceIfc mit getObject() nachladen. Außerdem kann er immer eine
 * GuiRepresentation und eine GuiRepresentationLong liefern, die zu Anzeigezwecken benutzt werden kann.
 * Im Client-DatenModel werden Proxy's auch als SuchSchlüssel in Tree's und Tables verwendet. Mann kann sozusagen
 * einen Proxy als Handle für ein Data-Object verwenden. Die equals-Methode ist mit größter Sorgfalt zu pflegen
 * da von ihr das Finden in Tree's Tables abhängt.
 */
public class DataProxy implements Serializable {
    private Class type;                     // der Typ (DataClass)
    private Data data;                      // das eventuell geladene DataObject
    public String guiRepresentation;       // visuelle Darstellung des Proxys.
    public String guiRepresentationLong;    // visuelle Darstellung des Proxys in langform.
    private String oidString;               // aus der DB generierte OID
    private int version;                    // für optimistic locking
    private String errorText;               // falls es Fehler beim mapen von Tabellen gibt, kann hier schon gleich im Proxy eine Info abgelegt werden..
    private String clientErrorText;       // Clientseitig ermittelter Fehlertext. Wir separat gehalten um den serverseitigen nicht zu überschreiben.

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    public boolean hasErrorText() {
        return errorText != null || clientErrorText != null;
    }

    public String getErrorText() {
        return errorText;
    }


    static final long serialVersionUID = -1L;  // verhindert Serialization-Mismatch zwische Client/RmiServiceIfc
    private static int uniqueCount = 0;

    /**
     * Flache Kopie des Proxys (ohne Data).
     *
     * @return
     */
    public DataProxy flatCopy() {
        DataProxy dp = new DataProxy(type, oidString, guiRepresentation, guiRepresentationLong);
        dp.version = this.version;
        return dp;
    }

    public boolean hasNewData() {
        return (hasData() && getData(false).isNew());
    }

    private DataProxy(Class c, String oidString, String guiRepresentation, String guiRepresentationLong) {
        setOidString(oidString);
        this.guiRepresentation = guiRepresentation;
        this.guiRepresentationLong = guiRepresentationLong;
        this.data = null;
        setType(c);
    }

    public DataProxy(Class c, String oidString, String guiRepresentation) {
        this(c, oidString, guiRepresentation, null);
    }

    public DataProxy(Class c) {
        this(c, null, "", null);
    }

    public DataProxy(Data data) {
        setData(data);
    }

    public void setType(Class type) {
        this.type = type;
    }

    private String getDataInfo() {
         return (data == null ? " NULL " : " : LOADED ");
    }

    /*
     * default Implementierung. Wenn dieser Proxy an einem Data-Object hängt, wird dessen guiRepresentationLong
     * benutzt.
     */
    public String getGuiRepresentationLong() {
        if (hasData()) {     // falls daten dranhängen könnten sie noch aktueller sein, darum.
            return getData(false).getGuiRepresentationLong();
        }

        if (guiRepresentationLong == null) {
            return "null";//Util.getUnqualifiedClassName(getType()) + ":" + getOidString() + "." + getVersion() + getDataInfo();
        } else {
            return guiRepresentationLong;
        }
    }

    /*
     * default Implementierung. Wenn dieser Proxy an einem Data-Object hängt, wird dessen guiRepresentation
     * benutzt.
     */
    public String getGuiRepresentation() {
        if (hasData()) {     // falls daten dranhängen könnten sie noch aktueller sein, darum.
            return getData(false).getGuiRepresentation();
        }

        if (guiRepresentation == null) {
            return Util.getUnqualifiedClassName(getType()) + ":" + getOidString() + "." + getVersion() + getDataInfo();
        } else {
            if(guiRepresentation.length()==0){
                return "???";
            }
            return guiRepresentation;
        }
    }


    /**
     * hat dieser Proxy schon ein Data-Object ?
     */
    public boolean hasData() {
        return data != null;
    }

    public Data getData(boolean forceReload) {
//        if (data == null || forceReload) {
//            if (oidString != null) {
//                try {
//                    SelectionModel sm = new SelectionModel(new Selection(this));
//                    ServerAction sa = ActionFactory.createServerAction(GetBoAction.class, sm);
//                    sa = sa.start();
//
//                    if (sa.getSelectionModel().getSingleSelection().hasData()) {
//                       data = sa.getSelectionModel().getSingleSelection().getData();
//                    } else {
//                        Log.write("null Data from  server", new Throwable());
//                        new Throwable().printStackTrace();
//                        return Data.createEmptyData(getType());
//                    }
//                } catch (Throwable e) {
//                    Log.write("Data from Proxy could not be retreived from server", e);
//                }
//            } else {
//                System.out.println("***************** EMPTY DATA FROM PROXY (" + getType().getName() + ") ******************");
//                new Throwable().printStackTrace();
//                return Data.createEmptyData(getType());
//            }
//        }
        return data;
    }

    public void setData(Data data) {
        if (data == null) {
            this.data = null;
            setOidString(null);
            this.version = 0;
            this.type = null;
            this.guiRepresentation = null;
            this.guiRepresentationLong = null;
        } else {
            this.data = data;
            setOidString(data.getOidString());
            this.version = data.getVersion();
            this.guiRepresentation = data.getGuiRepresentation();
            this.guiRepresentationLong = data.getGuiRepresentationLong();
            setType(data.getClass());
        }
    }

    public String getOidString() {
        return oidString;
    }

    public String toString() {
       return getGuiRepresentation();
    }

    public String asString() {
       return getOidString() + ":" + getType() + ":" + getVersion()+":"+getGuiRepresentation()+":"+getGuiRepresentationLong();
    }
    
    public java.lang.Class getType() {
        return type;
    }

    public void setGuiRepresentation(String guiRepresentation) {
        this.guiRepresentation = guiRepresentation;
        if (hasData()) {     // falls daten dranhängen sollten sie auch updated werden.
            getData(false).setGuiRepresentation(guiRepresentation);
        }

    }

    public void setGuiRepresentationLong(String guiRepresentationLong) {
        this.guiRepresentationLong = guiRepresentationLong;
    }


    public void setOidString(String oidString) {
        this.oidString = oidString;
    }


    public void unload() {
        this.data = null;
    }


    public Data createData() {
        return Data.createEmptyData(getType());
    }

    public boolean hasReference() {
        return (oidString != null);
    }

    /*
     *Die equals-Methode ist mit größter Sorgfalt zu pflegen da von ihr das Finden in
     * Tree's Tables abhängt.
     */
    public boolean equals(Object other) {
        if (other == null) return false;
        if (this == other) return true;
        if (!(other instanceof DataProxy)) return false;
        DataProxy o = (DataProxy) other;
        if (oidString == null && o.oidString != null) return false;
        if (o.oidString == null && oidString != null) return false;

        if (o.oidString != null && oidString != null) {
            return (oidString.equals(o.oidString));
        }

        if (version != o.version) return false;
//        if(containerIndex!=o.containerIndex)return false;
        if (!type.equals(o.type)) return false;
        //        if(hasData()&&o.hasData()){
        //           if(data!=o.data)return false;
        //        }
        return true;
    }

    public int hashCode() {
        return oidString == null ? 17 : oidString.hashCode()
                + version + type.hashCode();
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    /*
     * sorgt dafür das dieser Proxy auch eindeutig ist auch wenn er noch keine oid hat, also
     * aus einem neuen Data entstanden ist. Vieleicht sollte mann aber lieber den oidString
     * modifizieren, das würde die equals Methode vereinfachen.
     */
    public void makeUnique() {
        if (oidString == null && version == 0) {
            version = --uniqueCount;
        }
    }

    public DataReference toReference() {
        return new DataReference(this);
    }

    public boolean isReference() {
        return false;
    }

    public void setClientErrorText(String s) {
        clientErrorText = s;
    }

    public String getClientErrorText() {
        return clientErrorText;
    }

    public String  asPersistentXML() {
        return "<proxy>"+
               Util.asDBXMLString("type",getType().getName(),false) +
               Util.asDBXMLString("guiRep",getGuiRepresentation(),false) +
               Util.asDBXMLString("guiRepLong",getGuiRepresentationLong(),false) +
               Util.asDBXMLString("oid",getOidString(),false) +
               "</proxy>" ;
    }

    public void readPersistentXML(XmlElement tree, String parentPath) throws ClassNotFoundException {
       String type = tree.findUnmarkedChild(parentPath+".proxy.type").getText();
       setType(Class.forName(type));
       setGuiRepresentation(tree.findUnmarkedChild(parentPath+".proxy.guiRep").getText());
       setGuiRepresentationLong(tree.findUnmarkedChild(parentPath+".proxy.guiRepLong").getText());
       setOidString(tree.findUnmarkedChild(parentPath+".proxy.oid").getText());
    }


}
