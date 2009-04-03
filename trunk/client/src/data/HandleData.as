package data {
import framework.cs.Data;

[RemoteClass(alias="de.ama.server.bom.HandleData")]
public class HandleData extends Data{

  public var path:String ;
  public var tags:String ;
  public var lastUser:String ;
  public var lastmodified:Number;
  public var size:Number;
  public var userId:Number;
  public var x:int    ;
  public var y:int    ;


}
}