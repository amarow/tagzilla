
package de.ama.framework.data;

import de.ama.util.Util;


public class DataTable implements java.io.Serializable {
    public Data protoType;
    public Data[] collection = new Data[0];
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

        for (int i = 0; i < collection.length; i++) {
            Data data = (Data) collection[i];
            sb.append( data.asXMLString("element",printFormat) );
        }
        sb.append(indent+"</"+Util.saveToString(getName())+"Table>").append(Util.CRLF);
        Data.level--;
        return sb.toString();
    }

    public int size() {
        return collection.length;
    }

    public Object get(int i) {
        return collection[i];
    }

    public void add(Data data) {
        Data[] datas = new Data[collection.length+1];
        for (int i = 0; i < collection.length; i++) {
            Data d = collection[i];
            datas[i]=d;
        }
        datas[collection.length]=data;
        collection = datas;
    }

    @Override
    public String toString() {
        if(protoType!=null){
            return "DataTable of "+ Util.getUnqualifiedClassName(protoType.getClass())+" size="+size();
        } else {
            return "DataTable size="+size();
        }
    }
}
