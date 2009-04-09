package actions
{
import data.HandleData;

import components.*;

import framework.cs.*;
import framework.cs.DataTable;

[RemoteClass(alias="de.ama.server.actions.GetHandlesAction")]
public class GetHandlesAction extends ActionScriptAction{
    public var handle:HandleData = new HandleData();
    public var path:String;
    public var tag:String;
    public var count:Number;


    override public function onAfterCall(context:ActionStarter):void {
        var doc:Object = context.invoker.document.parentDocument;
        var grid:HandlesGrid = doc["handlesGrid"];
        if (data is DataTable && DataTable(data).collection != null) {
            grid["handles"] = DataTable(data).collection;
            var desk:Desk = context.invoker.document;
            desk.count.text = "" + count;
            desk.count.setStyle("color", count > 200 ? "red" : "green");
        }
    }
}
}