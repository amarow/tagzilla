package actions
{
import components.Desk;
import flash.display.DisplayObjectContainer;
[RemoteClass(alias="de.ama.server.actions.DeskAction")]
public class DeskAction extends ActionScriptAction{
	
    public var deskName:String;


    override public function onAfterCall(context:ActionContext):void {
       var desk:Desk = Desk(context.invoker);
       var parent:DisplayObjectContainer = desk.parent;
       parent.removeChild(desk);
       parent.addChild(Desk(data)) ;
    }
}
}