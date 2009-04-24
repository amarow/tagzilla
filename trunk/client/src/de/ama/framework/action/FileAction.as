package de.ama.framework.action
{
import de.ama.framework.util.Util;
[RemoteClass(alias="de.ama.server.actions.FileAction")]
public class FileAction extends ActionScriptAction{

    public var fileName:String;


    override public function onAfterCall(context:ActionStarter):void {
        Util.showMessage("File was uploaded OK JUHU WOW JEA ...");
    }
}
}


