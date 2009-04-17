package de.ama.framework.action {
import de.ama.framework.gui.*;
import de.ama.framework.util.Environment;

import de.ama.framework.util.Util;

[RemoteClass(alias="de.ama.framework.action.LoginAction")]
public class LoginAction extends ActionScriptAction{
    public var _user:String;
    public var _pwd:String;


    override public function onAfterCall(context:ActionStarter):void {
        if ( success ) {
            Environment.registerLoginData(this);
        }

        if(!Util.isEmpty(message)){
           Util.showMessage(message); 
        }

        if(context.invoker is LoginDialog){
            var ld:LoginDialog = LoginDialog(context.invoker);
            ld.finish(this);
        }
    }

    public function get success():Boolean {
        return ( userId>0);
    }


}
}