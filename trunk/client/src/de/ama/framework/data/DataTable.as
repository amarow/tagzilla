package de.ama.framework.data {
import de.ama.framework.util.Util;

import mx.collections.ArrayCollection;

[RemoteClass(alias="de.ama.framework.data.DataTable")]
public class DataTable {
	public var collection:ArrayCollection = new ArrayCollection();
    public var protoType:Data;


    public function DataTable(protoType:Data=null) {
        this.protoType = protoType;
    }

    public function readArrayCollection(src:ArrayCollection, clazz:Class=null):void{
    	if(clazz==null) {clazz = getTypeClass(); }
        collection.removeAll();
        var data:Data;
        for each (var obj:Object in src){
            data = new clazz();
            data.readProperties(obj);
            collection.addItem(data);
        }
    }

    public function writeArrayCollection(dst:ArrayCollection, clazz:Class):void{
        dst.removeAll();
        var obj:Object;
        for each (var data:Data in collection){
            obj = new clazz();
            data.writeProperties(obj);
            dst.addItem(obj);
        }
    }

    public function addItem(data:Data):void{
        collection.addItem(data);
    }

    public function get length():int{
        return collection.length;
    }

    public function getTypeClass():Class{
        return Util.getClass(protoType);
    }


    public function getItemAt(i:int):Data {
        return Data(collection.getItemAt(i));
    }

    public function clear():void{
        collection.removeAll();
    }
}
}