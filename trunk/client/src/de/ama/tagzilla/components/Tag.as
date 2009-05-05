package de.ama.tagzilla.components {
import de.ama.framework.action.ActionStarter;
import de.ama.framework.data.DataTable;
import de.ama.framework.util.Callback;
import de.ama.tagzilla.actions.GetHandlesAction;
import de.ama.tagzilla.actions.TagAction;
import de.ama.tagzilla.data.TagData;

import flash.events.MouseEvent;
import flash.geom.Point;

import de.ama.framework.util.Util;

import mx.containers.Canvas;
import mx.controls.Label;
import mx.events.DragEvent;
import mx.managers.DragManager;
import mx.managers.PopUpManager;

public class Tag extends Canvas   {

    private var dto:TagData;

    private var _oldpos:Point;
    private var mylabel:Label = new Label();
    private var openConfig:Label = new Label();

    public function Tag(aData:TagData = null) {
        dto = aData;
        if (dto == null) {
            dto = new TagData();
        }

        x = dto.x;
        y = dto.y;
        bgcolor = dto.bgcolor;
        path = dto.path;
        tag = dto.tag;

        super.height = 20 + dto.weight;
        _oldpos = new Point(x, y);


        super.setStyle("color", "black");
        super.setStyle("textAlign", "center");
        super.setStyle("cornerRadius", "8");
        super.setStyle("borderStyle", "solid");
        super.setStyle("borderColor", "white");

        super.addEventListener(MouseEvent.CLICK, onTagClick);
        super.addEventListener(MouseEvent.MOUSE_DOWN, startDragging);
        super.addEventListener(MouseEvent.MOUSE_UP, stopDragging);
        super.addEventListener(DragEvent.DRAG_DROP, dragDropHandler);
        super.addEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);

        mylabel.x = 5;
        mylabel.setStyle("verticalCenter", "0");
        addChild(mylabel);

        openConfig.text = "+";
        openConfig.setStyle("right", "1")
        openConfig.setStyle("verticalCenter", "0");
        openConfig.addEventListener(MouseEvent.CLICK, openConfigClick);
        addChild(openConfig);
    }


    public function get tag():String {
        return dto.tag;
    }

    public function set tag(val:String):void {
        if (Util.isEmpty(val)) return;
        dto.tag = val;
        dto.path=null;
        mylabel.text = Util.shrinkString(tag, 20);
        width = 28 + tag.length * 8;
    }

    public function get path():String {
        return dto.path;
    }

    public function set path(val:String):void {
        if (Util.isEmpty(val)) return;
        dto.path = val;
        dto.tag=null;
        mylabel.text = Util.shrinkString(path, 20);
        width = 28 + path.length * 8;
    }


    public function onTagClick(event:MouseEvent):void {

        if (x != _oldpos.x || y != _oldpos.y) {
            _oldpos.x = x;
            _oldpos.y = y;
            return;
        }

        loadHandles()

    }

    public function loadHandles():void {
        var a:GetHandlesAction = new GetHandlesAction();
        a.path = path+"*";
        a.tag = tag;
        ActionStarter.instance.execute(a, new Callback(this,showResult));
    }

    private function showResult(action:GetHandlesAction):void {
        if (action.data is DataTable && DataTable(action.data).collection != null) {
            var doc:Object = document.parentDocument;
            var grid:HandlesGrid = doc["handlesGrid"];
            grid.handles = DataTable(action.data).toArrayCollection();
        }
    }

    public function showConfig():void {
        var props:TagProps = TagProps(PopUpManager.createPopUp(parent, TagProps, true));
        props.x = this.x;
        props.y = this.y + this.height + 5;
        if (props.x + props.width > parent.width) {
            props.x = parent.width - props.width;
        }
        if (props.y + props.height > parent.height) {
            props.y = parent.height - props.height;
        }
        props.tag = this;
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

    private function dragEnterHandler(event:DragEvent):void {
        if (event.dragSource.hasFormat('items')) {
            var dropTarget:Tag = event.currentTarget as Tag;
            DragManager.acceptDragDrop(dropTarget);
        }

    }

    private function dragDropHandler(event:DragEvent):void {
        if (event.dragSource.hasFormat('items')) {
            var a:TagAction = new TagAction();
            a.data = event.dragSource.dataForFormat('items');
            a.tag = tag;
            ActionStarter.instance.execute(a, this);
        }
    }


    public function get bgcolor():uint {
        return getStyle("backgroundColor");
    }

    public function set bgcolor(val:uint):void {
        setStyle("backgroundColor", val);
    }


    public function getData():TagData {
        if (dto == null)dto = new TagData();
        dto.bgcolor = bgcolor;
        dto.x = x;
        dto.y = y;
        dto.tag = tag;
        dto.path = path;
        return dto;
    }
}
}