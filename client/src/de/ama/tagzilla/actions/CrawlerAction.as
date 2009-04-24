package de.ama.tagzilla.actions
{
import de.ama.framework.action.ActionScriptAction;
import de.ama.framework.action.ActionStarter;
import de.ama.framework.data.DataTable;
import de.ama.tagzilla.components.AdminPage;
import de.ama.tagzilla.data.CrawlerData;

[RemoteClass(alias="de.ama.server.actions.CrawlerAction")]
public class CrawlerAction extends ActionScriptAction
{
    public var crawler:CrawlerData;
    public var code:String;


    override public function onAfterCall(context:ActionStarter):void {
        var ap:AdminPage = AdminPage(context.invoker);

        if (data is DataTable) {
            ap.crawlers = DataTable(data).toArrayCollection();
        }

        ap.setStatusText(message);
    }
}
}