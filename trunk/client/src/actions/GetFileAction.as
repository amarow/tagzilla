package actions
{
import components.Handle;

import framework.cs.ActionScriptAction;
import framework.cs.ActionStarter;

[RemoteClass(alias="de.ama.server.actions.GetFileAction")]
public class GetFileAction extends ActionScriptAction{

    public var fileName:String;


    override public function onAfterCall(context:ActionStarter):void {
        var handle:Handle = Handle(context.invoker);
        handle.setData(data);
    }
}
}


