package components {
import actions.*;

import flash.events.MouseEvent;
import flash.geom.Point;

import mx.controls.Button;
import mx.events.DragEvent;
import mx.managers.DragManager;
import mx.managers.PopUpManager;

[RemoteClass(alias="de.ama.server.bom.Tag")]
public class Tag extends Button{
    public var oid:Number;
    public var version:Number;
    
    public var _weight:int;
    public var _tag:String;
    public var _path:String;
    private var _oldpos:Point;


    public function Tag(_tag:String = "", _weight:int = 5) {
        super();
        super.x = 50;
        super.y = 50;
        _oldpos = new Point(x, y);

        _weight = _weight
        tag = _tag;

        super.setStyle("color", "black");
        super.setStyle("textAlign", "center");

        super.addEventListener(MouseEvent.CLICK, onTagClick);
        super.addEventListener(MouseEvent.MOUSE_DOWN, startDragging);
        super.addEventListener(MouseEvent.MOUSE_UP, stopDragging);
        super.addEventListener(DragEvent.DRAG_DROP, dragDropHandler);
        super.addEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);
    }


    public function get tag():String {
        return _tag;
    }

    public function set tag(val:String):void {
        _tag = val;
        super.label = Util.shrinkString(_tag, 20);
        super.width = 28 + _tag.length * 8;
    }

    public function get path():String {
        return _path;
    }

    public function set path(val:String):void {
        _path = val;
        super.label = Util.shrinkString(_path, 20);
        super.width = 28 + _path.length * 8;
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

        if (event.altKey || event.localX > (width - 10)) {
            showConfig();
        } else {
            loadHandles()
        }


    }

    public function loadHandles():void {
        var a:GetHandlesAction = new GetHandlesAction();
        a.path = path;
        a.tag = tag;
        ActionContext.instance.execute(a, this);
    }

    public function showConfig():void {
        var props:TagProps = TagProps(PopUpManager.createPopUp(parent, components.TagProps, true));
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