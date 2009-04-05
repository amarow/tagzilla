package data {
import framework.cs.Data;

import mx.utils.ColorUtil;

[RemoteClass(alias="de.ama.server.bom.TagData")]
public class TagData extends Data{
	
    public var path:String ;
    public var tag:String="Tag" ;
    public var weight:int=5;
    public var bgcolor:uint=0xffffff;
    public var x:int=100;
    public var y:int=100;
}
}