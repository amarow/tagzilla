package de.ama.tagzilla.actions
{
import de.ama.framework.action.ActionScriptAction;
import de.ama.tagzilla.data.CrawlerData;

[RemoteClass(alias="de.ama.server.actions.CrawlerAction")]
	public class CrawlerAction extends ActionScriptAction
	{
		public var crawler:CrawlerData;


    }
}