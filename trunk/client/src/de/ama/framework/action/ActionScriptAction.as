package de.ama.framework.action {
[RemoteClass(alias="de.ama.framework.action.ActionScriptAction")]
public class ActionScriptAction {

    public var userId:Number;
    public var catalog:String;

    public var message:String;
    public var detailErrorMessage:String;
    public var data:Object;

    public var versionMismatch:Boolean;
    public var dontCommit:Boolean;


    public function onBeforeCall(context:ActionStarter):void {

    }

    public function onAfterCall(context:ActionStarter):void {

    }
}
}