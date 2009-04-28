package de.ama.tagzilla.components {
import de.ama.framework.action.ActionScriptAction;
import de.ama.framework.action.ActionStarter;
import de.ama.framework.action.FileAction;
import de.ama.framework.util.BroadcastEvent;
import de.ama.framework.util.Callback;
import de.ama.framework.util.FileManager;
import de.ama.framework.util.Util;
import de.ama.tagzilla.components.img.Images;
import de.ama.tagzilla.data.DeskHandleData;

import flash.events.MouseEvent;

import mx.containers.Canvas;
import mx.controls.Button;
import mx.controls.Label;

public class DeskHandle extends Canvas   {


    private var dto:DeskHandleData;

    private var mylabel:Label = new Label();
    private var saveButton:Button ;
    private var newButton:Button ;
    private var deleteButton:Button ;
    private var openButton:Button ;
    private var _incomplete:Boolean = false;

    public function DeskHandle(aData:DeskHandleData = null) {
        dto = aData;
        if (dto == null) {
            dto = new DeskHandleData();
            path = "move near other file, to retreive a location";
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
        super.addEventListener(BroadcastEvent.BROADCAST, fileUploadCompleted);
        //  super.addEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);


        mylabel.x = 2;
        mylabel.setStyle("verticalCenter", "0");
        mylabel.setStyle("color", "0x003333");
        mylabel.setStyle("textAlign", "right");
        addChild(mylabel);

        openButton = Button(addChild(createButton("open", Images.OPEN_PNG, openButtonClick, "2")));
        saveButton = Button(addChild(createButton("save", Images.BACK_PNG, saveButtonClick, "20")));
        deleteButton = Button(addChild(createButton("delete", Images.STOP_PNG, deleteButtonClick, "40")));
        newButton = Button(addChild(createButton("new", Images.NEW_PNG, newButtonClick, "58")));
    }


    private function newButtonClick(e:MouseEvent):void {
        var handle:DeskHandle = new DeskHandle();
        this.parent.addChild(handle);
        handle.x = this.x;
        handle.y = this.y+22;
        handle.incomplete = true;
        handle.path = extractServerPath();
    }

    private function extractServerPath():String {
        return path.substr(0,path.lastIndexOf("/"));
    }


    public function openButtonClick(e:MouseEvent):void {
        new FileManager().showFile(path);
    }

    private function deleteButtonClick(e:MouseEvent):void {
        this.parent.removeChild(this);
    }

    private function saveButtonClick(e:MouseEvent):void {
        var fm:FileManager = new FileManager();
        fm.uploadFile(path+"/",new Callback(this, fileUploadCompleted ));
    }

    private function fileUploadCompleted(fm:FileManager):void {
        path=fm.serverPath;
        incomplete=false;
    }

    private function createButton(tip:String, png:Class, callback:Function, pos:String):Button {
        var ret:Button = new Button();
        ret.label = tip;
        ret.id = tip;
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

    public function get incomplete():Boolean {
        return _incomplete;
    }

    public function set incomplete(b:Boolean):void {
        _incomplete=b;
        if(_incomplete){
            mylabel.setStyle("color", "red");
            openButton.visible = false;
            newButton.visible = false;
        } else {
            mylabel.setStyle("color", "0x003333");
            openButton.visible = true;
            newButton.visible = true;
        }

    }

    private function startDragging(event:MouseEvent):void {
        //        var ds:DragSource = new DragSource();
        //        ds.addData(this, "deskhandle");
        //        DragManager.doDrag(this, ds, event)
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

    public function needsPath():Boolean {
        return (path.indexOf("move near other") > 0);
    }


}
}