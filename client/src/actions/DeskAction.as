package actions
{
import components.Desk;

import data.DeskData;

import framework.cs.*;

[RemoteClass(alias="de.ama.server.actions.DeskAction")]
public class DeskAction extends ActionScriptAction{
	
    public var save:Boolean;

    override public function onBeforeCall(context:ActionStarter):void {
      var desk:Desk = Desk(context.invoker);
      data = desk.getData();
    }

    override public function onAfterCall(context:ActionStarter):void {
       var desk:Desk = Desk(context.invoker);
       desk.setData(DeskData(data));
    }
}
}