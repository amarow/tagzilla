package de.ama.tagzilla.actions
{
import de.ama.framework.action.ActionScriptAction;
import de.ama.framework.action.ActionStarter;
import de.ama.framework.data.DataTable;
import de.ama.tagzilla.data.HandleData;
import de.ama.tagzilla.components.*;

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
            grid["handles"] = DataTable(data).toArrayCollection();
            var desk:Desk = context.invoker.document;
            desk.count.text = "" + count;
            desk.count.setStyle("color", count > 200 ? "red" : "green");
        }
    }
}
}