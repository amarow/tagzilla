package actions
{
import components.*;
import bom.DataTable;
import mx.collections.ArrayCollection;

[RemoteClass(alias="de.ama.server.actions.GetHandlesAction")]
public class GetHandlesAction extends ActionScriptAction{
    public var handle:Handle=new Handle();
    public var path:String;
    public var tag:String;


    override public function onAfterCall(context:ActionContext):void {
		if(data!=null && data is ArrayCollection){
	        var doc:Object = context.invoker.document.parentDocument;
	        var grid:HandlesGrid = doc["handlesGrid"];
            var handles:ArrayCollection = new ArrayCollection();
            DataTable(data).writeArrayCollection(handles, Handle);
	        grid["handles"] = handles;
		}
    }
}
}