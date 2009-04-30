package de.ama.framework.action {
import de.ama.framework.data.Data;
import de.ama.framework.gui.EditPanel;
import de.ama.framework.util.Util;

[RemoteClass(alias="de.ama.framework.action.DataAction")]
public class DataAction extends ActionScriptAction{


    override public function onAfterCall(context:ActionStarter):void {

       var ep:EditPanel = Util.findParentEditPanel(context.invoker);
       if(ep!=null){
          ep.setData(Data(data)) 
       }

    }

}
}