package actions
{
import data.HandleData;

import components.*;

import framework.cs.*;
import framework.cs.DataTable;

[RemoteClass(alias="de.ama.server.actions.GetHandlesAction")]
public class GetHandlesAction extends ActionScriptAction{
    public var handle:HandleData=new HandleData();
    public var path:String;
    public var tag:String;


    override public function onAfterCall(context:ActionStarter):void {
        var doc:Object = context.invoker.document.parentDocument;
        var grid:HandlesGrid = doc["handlesGrid"];
        grid["handles"] = DataTable(data).collection;
    }
}
}