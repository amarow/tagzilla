package components {

import actions.*;

import flash.events.Event;
import flash.events.IOErrorEvent;
import flash.events.MouseEvent;
import flash.net.FileReference;

import framework.cs.ActionContext;
import framework.util.Util;

import mx.controls.Label;

public class Handle extends Label
{

    private var fr:FileReference=null;
	private var filedata:Object;


    public var size:Number;
    public var lastmodified:Number;
    public var lastUser:String;
    public var _path:String;
    public var _tags:String ="";




    public function Handle(path:String = "") {
        super.text = path;
        super.setStyle("color", "red");
        super.setStyle("textAlign", "right");

        super.addEventListener(MouseEvent.MOUSE_DOWN, startDragging);
        super.addEventListener(MouseEvent.MOUSE_UP, stopDragging);
        super.addEventListener(MouseEvent.CLICK, onClick);
    }

    public function onClick(e:MouseEvent):void {
    	
    	if(filedata==null){
	        var a:GetFileAction = new GetFileAction();
	        a.fileName = path;
	        ActionContext.instance.execute(a, this);
    	} else {
    		saveFile();
    	}
        
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


	public function setData(obj:Object):void{
		filedata = obj;
		setStyle("color","white");
		
	}

    public function saveFile():void {
        fr = new FileReference();
        fr.addEventListener(Event.COMPLETE, onFileSave);
        fr.addEventListener(Event.CANCEL, onCancel);
        fr.addEventListener(IOErrorEvent.IO_ERROR, onSaveError);
        fr.save(filedata , "demfile.txt");
    }

    private function onFileSave(e:Event):void {
        fr = null;
        filedata = null;
       	setStyle("color","red");
    }

    private function onCancel(e:Event):void {
        fr = null;
    }

    private function onSaveError(e:IOErrorEvent):void  {
        Util.showError("Error Saving File : " + e.text);
        fr = null;
    }

}
}