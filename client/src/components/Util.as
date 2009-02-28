package components
{
	import mx.core.Application;
	
	public class Util
	{
		public static function saveToString(o:Object,def:String=""):String {
		    if(o==null)return def;
		    return o.toString();
		}

       import mx.events.MenuEvent;
       import components.ListPane;
       import components.TreeEditor;
       import components.AdvanceTabNavigator;

       public static function handleMenuClick(evt:MenuEvent):void {
          var type:String = XML(evt.item).attribute("type")[0];
          var model:String = XML(evt.item).attribute("model")[0];
          var tabs:Object = Application.application.getChildByName("mainTabs");
          if(type == "lister"){
             var p:ListPane = new ListPane();
             p.label=model;
             AdvanceTabNavigator(tabs).addChild(p);
             AdvanceTabNavigator(tabs).selectedChild=p;
          }
          if(type == "editor"){
             var e:TreeEditor = new TreeEditor();
             e.label=model;
             AdvanceTabNavigator(tabs).addChild(e);
             AdvanceTabNavigator(tabs).selectedChild=e;
          }
       }

	}
}