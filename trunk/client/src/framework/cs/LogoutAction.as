package framework.cs {
import framework.util.Util;

[RemoteClass(alias="de.ama.server.actions.LogoutAction")]
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