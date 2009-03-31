
package de.ama.framework.data;

import de.ama.util.Util;

import java.util.ArrayList;
import java.util.List;


public class DataTable {
    public Data protoType;
    public List collection = new ArrayList();
    public boolean deleting;

    public DataTable() {
        protoType = null;
    }

    public DataTable(Data protoType) {
        this.protoType = protoType;
    }

    public String getName(){
        return Util.getUnqualifiedClassName(protoType.getClass());
    }

    public String asXMLString(boolean printFormat) throws IllegalAccessException {
        Data.level++;
        StringBuffer sb = new StringBuffer();
        String indent="";
        for(int x=0;x<Data.level;x++)
            indent +="  ";

        sb.append(indent+"<"+Util.saveToString(getName())+"Table>"+Util.CRLF);

        for (int i = 0; i < collection.size(); i++) {
            Data data = (Data) collection.get(i);
            sb.append( data.asXMLString("element",printFormat) );
        }
        sb.append(indent+"</"+Util.saveToString(getName())+"Table>").append(Util.CRLF);
        Data.level--;
        return sb.toString();
    }

    public int size() {
        return collection.size();
    }

    public Object get(int i) {
        return collection.get(i);
    }

    public void add(Data data) {
        collection.add(data);
    }
}
