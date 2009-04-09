package framework.cs {
import components.LoginDialog;
[RemoteClass(alias="de.ama.server.actions.LoginAction")]
public class LoginAction extends ActionScriptAction{
    public var _user:String;
    public var _pwd:String;


    override public function onAfterCall(context:ActionStarter):void {
        if ( success ) {
            Environment.registerLoginData(this);
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