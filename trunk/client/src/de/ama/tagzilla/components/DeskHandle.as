package de.ama.tagzilla.components {
import de.ama.framework.util.FileManager;
import de.ama.framework.util.Util;
import de.ama.tagzilla.data.DeskHandleData;

import flash.events.MouseEvent;
import flash.geom.Point;

import mx.containers.Canvas;
import mx.controls.Button;
import mx.controls.Image;
import mx.controls.Label;

public class DeskHandle extends Canvas   {

    [Embed(source="/de/ama/tagzilla/components/img/blog.png")]
    private static const OPEN_PNG:Class;

    [Embed(source="/de/ama/tagzilla/components/img/backward.png")]
    private static const BACK_PNG:Class;

    [Embed(source="/de/ama/tagzilla/components/img/stop.png")]
    private static const STOP_PNG:Class;

    private var dto:DeskHandleData;

    private var mylabel:Label = new Label();

    public function DeskHandle(aData:DeskHandleData = null) {
        dto = aData;
        if (dto == null) {
            dto = new DeskHandleData();
        }

        x = dto.x;
        y = dto.y;
        path = dto.path;

        super.height = 18;

        super.setStyle("backgroundColor", "0xFFFFCC");
        super.setStyle("backgroundAlpha", "0.9");
        super.setStyle("cornerRadius", "4");
        super.setStyle("borderStyle", "solid");
        super.setStyle("borderColor", "white");
        super.addEventListener(MouseEvent.MOUSE_DOWN, startDragging);
        super.addEventListener(MouseEvent.MOUSE_UP, stopDragging);


        mylabel.x = 2;
        mylabel.setStyle("verticalCenter", "0");
        mylabel.setStyle("color", "0x003333");
        mylabel.setStyle("textAlign", "right");
        addChild(mylabel);

        addChild(createButton("open",OPEN_PNG,openButtonClick,"2"));
        addChild(createButton("save",BACK_PNG,saveButtonClick,"20"));
        addChild(createButton("delete",STOP_PNG,deleteButtonClick,"40"));
    }

    public function openButtonClick(e:MouseEvent):void {
        new FileManager().showFile(path);
    }

    private function deleteButtonClick(e:MouseEvent):void {
        this.parent.removeChild(this);
    }

    private function saveButtonClick(e:MouseEvent):void {
        new FileManager().uploadFile(path);
    }


    private function createButton(tip:String, png:Class, callback:Function, pos:String):Button{
        var ret:Button = new Button();
        ret.label = tip;
        ret.width = 16;
        ret.height = 16;
        ret.setStyle("right", pos)
        ret.setStyle("verticalCenter", "0");
        ret.setStyle("upSkin", png);
        ret.setStyle("overSkin", png);
        ret.setStyle("downSkin", png);
        ret.addEventListener(MouseEvent.CLICK, callback);
        return ret; 
    }

    public function get path():String {
        return dto.path;
    }

    public function set path(val:String):void {
        if (Util.isEmpty(val)) return;
        dto.path = val;
        mylabel.text = val;
        width = val.length * 7 + 30;
    }

    private function startDragging(event:MouseEvent):void {
        super.startDrag();
    }

    private function stopDragging(event:MouseEvent):void {
        super.stopDrag();
    }

    public function getData():DeskHandleData {
        if (dto == null)dto = new DeskHandleData();
        dto.x = x;
        dto.y = y;
        dto.path = path;
        return dto;
    }
}
}