package actions
{
import components.*;

import mx.collections.ArrayCollection;

[RemoteClass(alias="de.ama.server.actions.TagAction")]
public class TagAction extends ActionScriptAction{

    public var tag:String;

    override public function onAfterCall(context:ActionContext):void {
		if(data!=null && data is ArrayCollection){
	        var doc:Object = context.invoker.document.parentDocument;
	        var grid:HandlesGrid = doc["handlesGrid"];
	        grid["handles"] = data;
		}
    }
}
}