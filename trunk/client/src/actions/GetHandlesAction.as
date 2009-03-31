package actions
{
import bom.HandleData;
import components.*;
import bom.DataTable;
import mx.collections.ArrayCollection;

[RemoteClass(alias="de.ama.server.actions.GetHandlesAction")]
public class GetHandlesAction extends ActionScriptAction{
    public var handle:HandleData=new HandleData();
    public var path:String;
    public var tag:String;


    override public function onAfterCall(context:ActionContext):void {
        var doc:Object = context.invoker.document.parentDocument;
        var grid:HandlesGrid = doc["handlesGrid"];
        grid["handles"] = DataTable(data).collection;
    }
}
}