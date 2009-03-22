package components {
import actions.*;

import flash.events.MouseEvent;

import flash.geom.Point;
import mx.controls.Button;
import mx.events.DragEvent;
import mx.managers.DragManager;
import mx.managers.PopUpManager;

public class Tag extends Button{

    public var _size:int;
    public var _tag:String;
    public var _path:String;

    public function Tag(_tag:String = "", _size:int = 5) {
        super();
        super.x = 50;
        super.y = 50;

        size = _size
        tag = _tag;

        super.setStyle("color", "black");
        super.setStyle("textAlign", "right");

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
        super.label = Util.shrinkString(val, 20);
        super.width = 18+label.length * 8;
    }

    public function get path():String {
        return _path;
    }

    public function set path(val:String):void {
        _path = val;
        super.label = Util.shrinkString(val, 20);
        super.width = 18+label.length * 8;
    }

    public function get size():int {
        return _size;
    }

    public function set size(val:int):void {
        _size = val;
        height = 20 + _size;
    }

    public function onTagClick(event:MouseEvent):void {

        if (event.localX > 10) {
            var a:GetHandlesAction = new GetHandlesAction();
            a.path = path;
            a.tag = tag;
            ActionContext.instance.execute(a, this);
        } else {
            if (event.localY > height / 2) {
                var props:TagProps = TagProps(PopUpManager.createPopUp(parent, components.TagProps, true));
                props.x = this.x;
                props.y = this.y + this.height+5;
                props.tag = this;
            }
        }


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