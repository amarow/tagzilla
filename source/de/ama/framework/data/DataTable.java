
package de.ama.framework.data;

import de.ama.util.Util;

import java.util.ArrayList;
import java.util.List;


public class DataTable implements java.io.Serializable {
    public List collection = new ArrayList();
    public boolean deleting;


    public String asXMLString(boolean printFormat) throws IllegalAccessException {
        Data.level++;
        StringBuffer sb = new StringBuffer();
        String indent="";
        for(int x=0;x<Data.level;x++)
            indent +="  ";

        sb.append(indent+"<DataTable>"+Util.CRLF);

        for (int i = 0; i < collection.size(); i++) {
            Data data = (Data) collection.get(i);
            sb.append( data.asXMLString("element",printFormat) );
        }
        sb.append(indent+"</DataTable>").append(Util.CRLF);
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
//        Data[] datas = new Data[collection.length+1];
//        for (int i = 0; i < collection.length; i++) {
//            Data d = collection[i];
//            datas[i]=d;
//        }
//        datas[collection.length]=data;
//        collection = datas;
        collection.add(data);
    }

    @Override
    public String toString() {
        return "DataTable size="+size();
    }
}
