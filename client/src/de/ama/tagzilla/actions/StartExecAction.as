package de.ama.tagzilla.actions
{
import de.ama.framework.action.ActionScriptAction;

[RemoteClass(alias="de.ama.server.actions.StartExecAction")]
public class StartExecAction extends ActionScriptAction{
	
    public var cmdline:String;
    public var waitUntilReady:Boolean;

}
}