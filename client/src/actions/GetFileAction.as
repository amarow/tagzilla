package actions
{
import components.Handle;
import framework.cs.ActionContext;
import framework.cs.ActionScriptAction;

[RemoteClass(alias="de.ama.server.actions.GetFileAction")]
public class GetFileAction extends ActionScriptAction{

    public var fileName:String;


    override public function onAfterCall(context:ActionContext):void {
        var handle:Handle = Handle(context.invoker);
        handle.setData(data);
    }
}
}


