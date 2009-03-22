package actions
{
import components.*;
[RemoteClass(alias="de.ama.server.actions.GetHandlesAction")]
public class GetHandlesAction extends ActionScriptAction{
    public var handle:Handle=new Handle();
    public var path:String;
    public var tag:String;


    override public function onAfterCall(context:ActionContext):void {

        var doc:Object = context.invoker.document.parentDocument;

        var grid:HandlesGrid = doc["handlesGrid"];
        grid["handles"]=data;


    }
}
}