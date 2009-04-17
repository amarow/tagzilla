package de.ama.tagzilla.data {
import de.ama.framework.data.Data;
import de.ama.framework.data.DataTable;

[RemoteClass(alias="de.ama.server.bom.DeskData")]
public class DeskData extends Data{
    public var objects:DataTable = new DataTable();
    public var name:String;
    public var sliderPos:int;
}
}