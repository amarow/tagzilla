package de.ama.framework.gui {
import de.ama.framework.data.Data;
import mx.containers.Canvas;
public class EditPanel extends Canvas{

        private var data:Data;
        private var path:String;

        public function getPath():String {
            return path;
        }

        public function setPath(val:String):void {
            path = val;
        }

        public function getData():Object {
            return data;
        }

        public function setData(val:Data):void {
            data = val;
        }

        public function addField(field:EditPanel):void {
            addChild(field);
        }

        public function getField(path:String):EditPanel {
            
            var childs:Array = getChildren();
            var i:int;
            var len:int=childs.length;
            var f:EditPanel=null;
            for(i=0; i<len; i++){
                if(childs[i] is EditPanel){
                    f=childs.getItemAt(i) as EditPanel;
                    if(f.path==path) return f;
                }
            }
            return null;
        }


}
}