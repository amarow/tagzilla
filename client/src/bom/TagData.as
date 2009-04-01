package bom {
import framework.cs.Data;

[RemoteClass(alias="de.ama.server.bom.TagData")]
public class TagData extends Data{
	
    public var path:String ;
    public var tag:String ;
    public var weight:int;
    public var x:int;
    public var y:int;
}
}