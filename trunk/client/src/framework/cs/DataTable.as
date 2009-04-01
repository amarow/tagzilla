package src.framework.cs {

import f

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

    public function getTypeClass():Class{
        return Util.getClass(protoType);
    }

}
}