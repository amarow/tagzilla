package de.ama.tagzilla.components {

import de.ama.framework.util.FileManager;
import de.ama.framework.util.Util;
import de.ama.tagzilla.data.HandleData;

import flash.events.MouseEvent;

import mx.controls.Label;

public class Handle extends Label
{

    private var dto:HandleData;


    public function Handle(aData:HandleData) {
        dto = aData;
        x = dto.x;
        y = dto.y;
        path = dto.path;

        super.setStyle("color", "red");
        super.setStyle("textAlign", "right");


        super.addEventListener(MouseEvent.MOUSE_DOWN, startDragging);
        super.addEventListener(MouseEvent.MOUSE_UP, stopDragging);
        super.addEventListener(MouseEvent.DOUBLE_CLICK, onDoubleClick);
        super.doubleClickEnabled=true;
    }

    public function onDoubleClick(e:MouseEvent):void {
        new FileManager().showFile(path);
    }


    public function get path():String {
        return dto.path;
    }

    public function set path(val:String):void {
        dto.path = val;
        text = Util.shrinkString(val, 30, "...");
    }


    private function startDragging(event:MouseEvent):void {
        super.startDrag();
    }

    private function stopDragging(event:MouseEvent):void {
        super.stopDrag();
    }


    public function getData():HandleData {
        dto.x = x;
        dto.y = y;
        return dto;
    }
}
}