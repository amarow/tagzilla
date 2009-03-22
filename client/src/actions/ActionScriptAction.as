package actions {
[RemoteClass(alias="de.ama.server.actions.ServerAction")]
public class ActionScriptAction {

    public var userId:Number;
    public var catalog:String;

    public var message:String;
    public var detailErrorMessage:String;
    public var data:Object;


    public function onAfterCall(context:ActionContext):void {

    }
}
}