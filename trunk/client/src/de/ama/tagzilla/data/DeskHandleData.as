package de.ama.tagzilla.data {
[RemoteClass(alias="de.ama.server.bom.DeskHandleData")]
public class DeskHandleData extends HandleData{


    public function DeskHandleData(hd:HandleData = null) {
        if (hd) {
            this.oidString = null;
            this.version = 0;
            this.path = hd.path;
            this.tags = hd.tags;
            this.lastmodified = hd.lastmodified;
        }
    }

    public var lastUser:String ;
    public var userId:Number;
    public var x:int    ;
    public var y:int    ;
}
}