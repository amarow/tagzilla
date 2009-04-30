package de.ama.framework.util
{
import de.ama.framework.gui.EditPanel;

import flash.display.DisplayObject;
import flash.display.DisplayObjectContainer;

import mx.controls.Alert;
import mx.core.ApplicationGlobals;
import mx.core.UIComponent;
import mx.utils.ObjectUtil;

public class Util
{
    private static var globalStore:Array = new Array();

    public static function saveToString(o:Object, def:String = ""):String {
        if (o == null)return def;
        return o.toString();
    }

    public static function shrinkString(str:String, l:int, pre:String = ""):String {
        if (str == null)return "";
        if (str.length > l) {
            return pre + str.substr(str.length - l);
        }
        return pre + str;
    }


    public static function findComponent(key:String):Object {
        return ApplicationGlobals.application.getChildByName(key);
    }

    public static function findParentEditPanel(comp:Object):EditPanel {
        if (comp == null) return null;
        if (comp is EditPanel) return EditPanel(comp);

        if (comp is DisplayObject) {
            if (comp.parent == null) return null;
            if (comp.parent is EditPanel) return EditPanel(comp.parent);
            return findParentEditPanel(UIComponent(comp.parent));
        }

        return null;
    }

    public static function getComponent(key:String):Object {
        var c:Object = findComponent(key);
        if (c == null) {
            showError("Component for id(" + key + ") not found")
        }

        return c;
    }

    public static function showMessage(m:String):void {
        Alert.show(m, "Message");
    }

    public static function showError(m:String):void {
        Alert.show(m, "Error");
    }


    public static function storeGlobal(key:String, val:Object):void {
        globalStore[key] = val;
    }

    public static function getGlobal(key:String):Object {
        return globalStore[key];
    }

    public static function mapProperties(src:Object, dst:Object):void {
        var info:Object = ObjectUtil.getClassInfo(src);

        for each(var key:String in info.properties) {
            try {
                dst[key] = src[key];
            } catch(e:Error) {
                // macht nichts
            }
        }
    }

    public static function getClass(obj:Object):Class {
        return obj.constructor;
    }


    public static function isEmpty(val:String):Boolean {
        if (val == null) return true;
        if (val.length == 0) return true;
        return false;
    }

    public static function isEqual(a:*, b:*, caseInsensitive:Boolean = false):Boolean {
        if (a == null && b == null) return true;

        if (a == null && b != null) return false;
        if (a != null && b == null) return false;

        if (a is String) {
            return ObjectUtil.stringCompare(a, b, caseInsensitive) == 0;
        }
        if (a is Date) {
            return ObjectUtil.dateCompare(a, b) == 0;
        }
        if (a is Number) {
            return ObjectUtil.numericCompare(a, b) == 0;
        }


        return false;
    }


}
}