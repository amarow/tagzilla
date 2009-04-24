package de.ama.tagzilla.components {
import de.ama.framework.util.FileManager;
import de.ama.framework.util.Util;
import de.ama.tagzilla.data.DeskHandleData;

import flash.events.MouseEvent;
import flash.geom.Point;

import mx.containers.Canvas;
import mx.controls.Label;

public class DeskHandle extends Canvas   {

    private var dto:DeskHandleData;

    private var mylabel:Label = new Label();
    private var openConfig:Label = new Label();

    public function DeskHandle(aData:DeskHandleData = null) {
        dto = aData;
        if (dto == null) {
            dto = new DeskHandleData();
        }

        x = dto.x;
        y = dto.y;
        path = dto.path;

        super.height = 18 ;

        super.setStyle("backgroundColor", "0x996633");
        super.setStyle("backgroundAlpha", "0.9");
//        super.setStyle("color", "white");
        super.setStyle("cornerRadius", "4");
        super.setStyle("borderStyle", "solid");
        super.setStyle("borderColor", "white");

//        super.addEventListener(MouseEvent.CLICK, onClick);
        super.addEventListener(MouseEvent.MOUSE_DOWN, startDragging);
        super.addEventListener(MouseEvent.MOUSE_UP, stopDragging);
//        super.addEventListener(DragEvent.DRAG_DROP, dragDropHandler);
//        super.addEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);


        mylabel.x = 5;
        mylabel.setStyle("verticalCenter", "0");
        mylabel.setStyle("color", "black");
        mylabel.setStyle("textAlign", "right");
        mylabel.addEventListener(MouseEvent.DOUBLE_CLICK, onDoubleClick);
        addChild(mylabel);

        openConfig.text = "+";
        openConfig.setStyle("right", "1")
        openConfig.setStyle("verticalCenter", "0");
        openConfig.addEventListener(MouseEvent.CLICK, openConfigClick);
        addChild(openConfig);
    }

    public function get path():String {
        return dto.path;
    }

    public function set path(val:String):void {
        if (Util.isEmpty(val)) return;
        dto.path = val;
        mylabel.text = val;
        width = val.length*7+20;
    }
    public function onDoubleClick(e:MouseEvent):void {
        new FileManager().showFile(path);
    }

    public function showConfig():void {
        new FileManager().showFile(path);
//        var props:TagProps = TagProps(PopUpManager.createPopUp(parent, TagProps, true));
//        props.x = this.x;
//        props.y = this.y + this.height + 5;
//        if (props.x + props.width > parent.width) {
//            props.x = parent.width - props.width;
//        }
//        if (props.y + props.height > parent.height) {
//            props.y = parent.height - props.height;
//        }
//        props.tag = this;
    }

    private function openConfigClick(event:MouseEvent):void {
        showConfig();
    }

    private function startDragging(event:MouseEvent):void {
        super.startDrag();
    }

    private function stopDragging(event:MouseEvent):void {
        super.stopDrag();
    }

    public function get bgcolor():uint {
        return getStyle("backgroundColor");
    }

    public function set bgcolor(val:uint):void {
        setStyle("backgroundColor", val);
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