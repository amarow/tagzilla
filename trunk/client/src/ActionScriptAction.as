package {
	import mx.collections.ArrayCollection;
	
[RemoteClass(alias="server.actions.ActionScriptAction")]
public class ActionScriptAction {
    public var serverActionName:String;
    public var userId:String;
    public var catalog:String;

    public var message:String;
    public var detailErrorMessage:String;
    public var data:Object;

}
}