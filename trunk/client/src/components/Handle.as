package components {
	
import actions.*;

import flash.events.MouseEvent;

import mx.controls.Label;
import flash.utils.IDataInput;
import flash.utils.IDataOutput;
import flash.utils.IExternalizable;

[RemoteClass(alias="de.ama.server.data.Handle")]
public class Handle extends Label implements IExternalizable
{

    public var oid:String;
    public var version:Number;

    public var size:Number;
    public var lastmodified:Number;
    public var lastUser:String;
    public var _path:String;
    public var _tags:String ="";


    public function readExternal(input:IDataInput):void {
        oid = input.readUTF();
        version = input.readInt();
        
        x = input.readInt();
        y = input.readInt();
        _tags = input.readUTF();
        _path = input.readUTF();
    }

    public function writeExternal(output:IDataOutput):void {
        output.writeUTF(oid);
        output.writeInt(version);

        output.writeInt(x);
        output.writeInt(y);
        output.writeUTF(_tags);
        output.writeUTF(_path);
    }



    public function Handle(path:String = "") {
        super.text = path;
        super.setStyle("color", "red");
        super.setStyle("textAlign", "right");

        super.addEventListener(MouseEvent.MOUSE_DOWN, startDragging);
        super.addEventListener(MouseEvent.MOUSE_UP, stopDragging);
        super.addEventListener(MouseEvent.CLICK, onClick);
    }

    public function onClick(e:MouseEvent):void {
        var a:StartExecAction = new StartExecAction();
        a.cmdline = "start winword "+path;
        ActionContext.instance.execute(a, this);
    }



    public function set path(val:String):void {
        _path = val;
        super.text = Util.shrinkString(val, 30, "...");
    }


    public function get path():String {
        return _path;
    }


    public function get tags():String {

        return Util.saveToString(_tags).replace("§",".");
    }

    public function set tags(val:String):void {
        _tags = val;
    }

    public function addTag(val:String):void {
        _tags = val;
    }

    private function startDragging(event:MouseEvent):void{
        super.startDrag();
    }

    private function stopDragging(event:MouseEvent):void{
        super.stopDrag();
    }

    

}
}