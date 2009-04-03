package actions
{
import data.DeskData;

import components.Desk;
import components.Tag;

import framework.cs.*;

import mx.collections.ArrayCollection;

[RemoteClass(alias="de.ama.server.actions.DeskAction")]
public class DeskAction extends ActionScriptAction{
	
    public var save:Boolean;

    override public function onBeforeCall(context:ActionStarter):void {
      var dd:DeskData = new DeskData();
      dd.name = "Thyssen";
      super.data = dd;	

      if(save){
        var desk:Desk = Desk(context.invoker);
        dd.objects.readArrayCollection(desk.objects);
      }

    }

    override public function onAfterCall(context:ActionStarter):void {
       var desk:Desk = Desk(context.invoker);

       var dd:DeskData = DeskData(super.data);
       var tags:ArrayCollection = new ArrayCollection();
       dd.objects.writeArrayCollection(tags, Tag);
       desk.name = dd.name;
       desk.objects = tags;
    }
}
}