package components {

import data.HandleData;

import flash.events.MouseEvent;

import framework.cs.FileManager;

import mx.controls.Label;

public class Handle extends Label
{

    private var dto:HandleData;


    public function Handle(aData:HandleData) {
        dto=aData;
        super.text = path;
        super.setStyle("color", "red");
        super.setStyle("textAlign", "right");

        super.addEventListener(MouseEvent.MOUSE_DOWN, startDragging);
        super.addEventListener(MouseEvent.MOUSE_UP, stopDragging);
        super.addEventListener(MouseEvent.CLICK, onClick);
    }

    public function onClick(e:MouseEvent):void {

         new FileManager().showFile(path);

    }


    public function get path():String {
        return dto.path;
    }

    public function set path(val:String):void{
        dto.path=val;

    }


    private function startDragging(event:MouseEvent):void {
        super.startDrag();
    }

    private function stopDragging(event:MouseEvent):void {
        super.stopDrag();
    }


    public function getData():HandleData {
        return dto;
    }
}
}