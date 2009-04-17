package de.ama.tagzilla.actions
{
import de.ama.framework.action.ActionScriptAction;
import de.ama.framework.action.ActionStarter;
import de.ama.tagzilla.components.Handle;

[RemoteClass(alias="de.ama.server.actions.GetFileAction")]
public class GetFileAction extends ActionScriptAction{

    public var fileName:String;


    override public function onAfterCall(context:ActionStarter):void {
        var handle:Handle = Handle(context.invoker);
//        handle.setData(data);
    }
}
}


