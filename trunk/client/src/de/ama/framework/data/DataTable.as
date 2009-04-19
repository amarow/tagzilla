package de.ama.framework.data {
import de.ama.framework.util.Util;

import mx.collections.ArrayCollection;

[RemoteClass(alias="de.ama.framework.data.DataTable")]
public class DataTable {
	public var collection:Array = new Array();
    public var protoType:Data;
    public var deleting:Boolean;


    public function DataTable(protoType:Data=null) {
        this.protoType = protoType;
    }

    public function fromArrayCollection(src:ArrayCollection):void{
        collection = src.source;
    }

    public function toArrayCollection(): ArrayCollection {
        return new ArrayCollection(collection);
    }


    public function transformFromArrayCollection(src:ArrayCollection, clazz:Class=null):void{
    	if(clazz==null) {clazz = getTypeClass(); }
        clear();
        var data:Data;
        for each (var obj:Object in src){
            data = new clazz();
            data.readProperties(obj);
            collection.push(data);
        }
    }

    public function transformToArrayCollection(dst:ArrayCollection, clazz:Class):void{
        dst.removeAll();
        var obj:Object;
        for each (var data:Data in collection){
            obj = new clazz();
            data.writeProperties(obj);
            dst.addItem(obj);
        }
    }

    public function addItem(data:Data):void{
        collection.push(data);
    }

    public function get length():int{
        return collection.length;
    }

    public function getTypeClass():Class{
        return Util.getClass(protoType);
    }


    public function getItemAt(i:int):Data {
        return Data(collection[i]);
    }

    public function clear():void{
        while(collection.length>0){ collection.pop(); }
    }
}
}