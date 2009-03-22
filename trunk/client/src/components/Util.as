package components
{
import mx.controls.Alert;
import mx.core.Application;
import mx.core.ApplicationGlobals;
import mx.events.MenuEvent;

public class Util
{
    private static var globalStore:Array = new Array();

    public static function saveToString(o:Object, def:String = ""):String {
        if (o == null)return def;
        return o.toString();
    }

    public static function shrinkString(str:String, l:int,pre:String=""):String {
        if (str == null)return "";
        if(str.length > l){
            return pre+str.substr(str.length-l);
        }
        return pre+str;
    }




    public static function findComponent(key:String):Object {
            return ApplicationGlobals.application.getChildByName(key);
    }

    public static function getComponent(key:String):Object {
        var c:Object = findComponent(key);
        if (c == null) {
            showError("Component for id(" + key + ") not found")
        }

        return c;
    }

    public static function showMessage(m:String):void {
        Alert.show(m,"Message");
    }

    public static function showError(m:String):void {
        Alert.show(m,"Error");
    }


    public static function storeGlobal(key:String, val:Object):void{
        globalStore[key]=val;
    }

    public static function getGlobal(key:String):Object{
        return globalStore[key];
    }



}
}