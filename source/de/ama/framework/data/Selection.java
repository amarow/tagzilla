/*
 * Selection.java
 *
 * Created on 1. Februar 2003, 23:15
 */

package de.ama.framework.data;

import java.io.Serializable;


/**
 *
 * @author  Andreas Marochow
 */
public class Selection implements Serializable{
    static final long serialVersionUID = -1L;
    private DataProxy proxy;
    private transient DataTable table;

    public Selection(DataTable table) {
        this.table = table;
    }

    public Class getType(){
        if(hasProxy()){
           return proxy.getType();
        }if(hasTable()){
           return getTable().getType();
        }
        throw new IllegalStateException("no Type in Selction");
    }

    public Selection(DataProxy proxy) {
        this.proxy=proxy;
    }

    public Selection(Data data) {
        this(data.getDataProxy());
    }

    public DataProxy getProxy() {
        return proxy;
    }

    public DataTable getTable() {
        return table;
    }

    public String getOidString(){
        if(hasProxy())
           return proxy.getOidString();
        else if(hasTable()){
            return table.getContainerId();
        }
        throw new RuntimeException("no oidString in Node !!!");
    }

    public int getVersion(){
        return getProxy().getVersion();
    }

    public void setData(Data data){
        proxy=data.getDataProxy();
    }

    public Data getData(){
        return getProxy().getData(false);
    }

    public Data reloadData(){
        return getProxy().getData(true);
    }

    public boolean hasData(){
        return (hasProxy() && proxy.hasData());
    }

    public boolean hasType(Class dataClass){
        return dataClass.isAssignableFrom(getType());
    }

    public boolean hasTable(){
        return (table!=null);
    }

    public boolean hasProxy(){
        return (proxy!=null);
    }

    public String toString() {
       String tmp;
       if(hasProxy()){
          tmp= getProxy().toString();
       }else if(hasTable()){
          tmp= getTable().toString();
        }else{
          tmp= "EMPTY SELECTION !";
       }
       tmp+=" hashCode="+hashCode();
       return tmp;
    }

}
