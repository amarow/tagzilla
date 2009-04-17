package de.ama.framework.action {
import de.ama.framework.util.Environment;

import de.ama.framework.util.Util;

[RemoteClass(alias="de.ama.framework.action.LogoutAction")]
public class LogoutAction extends ActionScriptAction{

    public  var success:Boolean;

    override public function onAfterCall(context:ActionStarter):void {

        if (success) {
            Environment.eraseLoginData(this);
            Util.showMessage("you are logged out , By");
        }
        
        Util.showMessage("logout failed");

    }


}
}