package framework.cs {
import framework.util.Util;

[RemoteClass(alias="de.ama.framework.data.Data")]
public class Data{
    public var oid:Number;
    public var version:int;

    public function readProperties(src:Object):void{
        Util.mapProperties(src,this);
    }

    public function writeProperties(dst:Object):void{
        Util.mapProperties(this,dst);
    }
}
}