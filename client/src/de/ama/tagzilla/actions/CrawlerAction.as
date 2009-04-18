package de.ama.tagzilla.actions
{
import de.ama.framework.action.ActionScriptAction;
import de.ama.framework.action.ActionStarter;
import de.ama.framework.data.DataTable;
import de.ama.tagzilla.data.CrawlerData;

import mx.core.Application;

[RemoteClass(alias="de.ama.server.actions.CrawlerAction")]
public class CrawlerAction extends ActionScriptAction
{
    public var crawler:CrawlerData;
    public var code:String;


    override public function onAfterCall(context:ActionStarter):void {

        if (data is DataTable) {
            Application.application.crawlers = DataTable(data);
        }

        Application.application.setStatusText(message);
    }
}
}