package de.ama.framework.gui {
import de.ama.framework.data.Data;

import mx.containers.Canvas;

public class EditPanel extends Canvas{

    private var _path:String;
    private var _fcount:int = 1;


    public function EditPanel(path:String = null) {
        _path = path;
        _fcount = 1;

    }

    public function get path():String {
        return _path;
    }

    public function set path(val:String):void {
        _path = val;
    }

    public function getData():Data {
        return Data(data);
    }

    public function setData(val:Data):void {
        data = val;
        var childs:Array = getChildren();
        var i:int;
        var len:int = childs.length;
        var f:EditField = null;
        for (i = 0; i < len; i++) {
            if (childs[i] is EditField) {
                f = childs[i];
                f.setValue(data[f.localpath]);
            }
        }
    }

    public function insertTextField(label:String, path:String = null, x:int = -1, y:int = -1):void {
        insertField(new EditField(label, path), x, y);
    }

    public function insertBoolField(label:String, path:String = null, x:int = -1, y:int = -1):void {
        insertField(new BoolField(label, path), x, y);
    }

    public function insertDateField(label:String, path:String = null, x:int = -1, y:int = -1):void {
        insertField(new DateField(label, path), x, y);
    }

    public function insertField(f:EditField, x:int = -1, y:int = -1):void {
        f.x = x > 0 ? x : 10;
        f.y = y > 0 ? y : ((_fcount++) * 20) + 5;
        addChild(f);
    }

    public function getEditField(path:String):EditField {
        var childs:Array = getChildren();
        var i:int;
        var len:int = childs.length;
        var f:EditField = null;
        for (i = 0; i < len; i++) {
            if (childs[i] is EditField) {
                f = EditField(childs.getItemAt(i));
                if (f.path == path) return f;
            }
        }
        return null;
    }

}
}