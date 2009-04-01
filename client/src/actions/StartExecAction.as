package actions
{
import framework.cs.ActionScriptAction;
[RemoteClass(alias="de.ama.server.actions.StartExecAction")]
public class StartExecAction extends ActionScriptAction{
	
    public var cmdline:String;
    public var waitUntilReady:Boolean;

}
}