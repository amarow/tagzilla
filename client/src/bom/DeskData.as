package bom {
import framework.cs.Data;
import framework.cs.DataTable;

[RemoteClass(alias="de.ama.server.bom.DeskData")]
public class DeskData extends Data{
    public var objects:DataTable = new DataTable(new TagData());
    public var name:String;
}
}