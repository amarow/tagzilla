package {
	import mx.collections.ArrayCollection;
	
[RemoteClass(alias="de.ama.server.actions.ActionScriptAction")]
public class ActionScriptAction {
    public var serverActionName:String;
    public var userId:Number;
    public var catalog:String;

    public var message:String;
    public var detailErrorMessage:String;
    public var data:Object;

}
}