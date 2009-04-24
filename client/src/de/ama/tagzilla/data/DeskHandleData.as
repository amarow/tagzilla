package de.ama.tagzilla.data {
import de.ama.framework.data.Data;
[RemoteClass(alias="de.ama.server.bom.DeskHandleData")]
public class DeskHandleData extends Data{

    public var path:String ;
    public var lastmodified:Number;
    public var size:Number;
    public var lastUser:String ;
    public var userId:Number;
    public var x:int    ;
    public var y:int    ;

    public function DeskHandleData(hd:HandleData = null) {
        if (hd) {
            this.oidString = null;
            this.version = 0;
            this.path = hd.path;
            this.lastmodified = hd.lastmodified;
        }
    }
}
}