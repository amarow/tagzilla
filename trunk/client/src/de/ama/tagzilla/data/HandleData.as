package de.ama.tagzilla.data {
import de.ama.framework.data.Data;

[RemoteClass(alias="de.ama.server.bom.HandleData")]
public class HandleData extends Data{
  public var path:String ;
  public var tags:String ;
  public var lastmodified:Number;
  public var size:Number;
}
}