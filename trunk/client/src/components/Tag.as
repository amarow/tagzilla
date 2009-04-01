package components {
import actions.*;

import flash.events.MouseEvent;
import flash.geom.Point;

import framework.cs.ActionContext;
import framework.util.Util;

import mx.containers.Canvas;
import mx.controls.Image;
import mx.controls.Label;
import mx.events.DragEvent;
import mx.managers.DragManager;
import mx.managers.PopUpManager;

public class Tag extends Canvas   {
    public var oid:String;
    public var version:int;
    
    public var _weight:int;
    public var _tag:String;
    public var _path:String;
    private var _oldpos:Point;
    private var mylabel:Label = new Label();
    private var openConfig:Label = new Label();

    public function Tag(_tag:String = "", _weight:int = 5) {
        super();
        

        super.x = 50;
        super.y = 50;
        super.width = 200;
        super.height=20+_weight;
        _oldpos = new Point(x, y);

        _weight = _weight
        tag = _tag;

        super.setStyle("color", "black");
        super.setStyle("textAlign", "center");
        super.setStyle("backgroundColor", "white");
        super.setStyle("cornerRadius", "8");
        super.setStyle("borderStyle", "solid");
        super.setStyle("borderColor", "#168DE1");

        super.addEventListener(MouseEvent.CLICK, onTagClick);
        super.addEventListener(MouseEvent.MOUSE_DOWN, startDragging);
        super.addEventListener(MouseEvent.MOUSE_UP, stopDragging);
        super.addEventListener(DragEvent.DRAG_DROP, dragDropHandler);
        super.addEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);
        
        mylabel.x=5; 
        mylabel.setStyle("verticalCenter","0");
        addChild(mylabel);

        openConfig.text="+";
        openConfig.setStyle("right","1")
        openConfig.setStyle("verticalCenter","0");
        openConfig.addEventListener(MouseEvent.CLICK, openConfigClick);
        addChild(openConfig);
    }


    public function get tag():String {
        return _tag;
    }

    public function set tag(val:String):void {
        _tag = val;
        mylabel.text = Util.shrinkString(_tag, 20);
        width = 28 + _tag.length * 8;
    }

    public function get path():String {
        return _path;
    }

    public function set path(val:String):void {
        _path = val;
        mylabel.text = Util.shrinkString(_path, 20);
        width = 28 + _path.length * 8;
    }

    public function get weight():int {
        return _weight;
    }

    public function set weight(val:int):void {
        _weight = val;
        height = 20 + _weight;
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
        a.path = path;
        a.tag = tag;
        ActionContext.instance.execute(a, this);
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
            ActionContext.instance.execute(a, this);
        }
    }
    
    

}
}