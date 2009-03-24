package actions
{
import components.*;

[RemoteClass(alias="de.ama.server.actions.StartExecAction")]
public class StartExecAction extends ActionScriptAction{
	
    public var cmdline:String;
    public var waitUntilReady:Boolean;

}
}