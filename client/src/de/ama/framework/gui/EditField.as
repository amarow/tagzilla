package de.ama.framework.gui {
import de.ama.framework.data.Data;
import de.ama.framework.util.Util;

import flash.events.Event;

import mx.containers.Canvas;
import mx.controls.Label;
import mx.controls.TextInput;
import mx.core.UIComponent;

public class EditField extends Canvas{

    protected var _label:Label;
    protected var _input:UIComponent;

    private var _fullpath:String;
    private var _localpath:String;
    private var _labelWidth:int = 100;


    public function EditField(caption:String="EditField", path:String=null) {
        super.width=400;
        super.height=25;

        createLabel(caption);
        createInput();
        _localpath = path;

        _input.setStyle("color","black");
        _input.addEventListener(Event.CHANGE,inputCanged);

        _label.setStyle("color","black");
        _label.setStyle("textAlign","right");
        _label.setStyle("paddingRight","8");
        _label.y=3;

    }

    protected function inputCanged(e:Event):void {
        var d:Data = editPanel.getData();
        d[_localpath] = getValue();

    }

    public function createLabel(caption:String):void{
        _label = new Label();
        _label.x = 5;
        _label.width = _labelWidth;

        label = caption;
        addChild(_label);
    }

    public function createInput():void{
        _input = new TextInput();
        _input.x = _labelWidth+10;
        _input.width = super.width - 15 - _labelWidth;
        addChild(_input);
    }

    public function set labelWith(w:int):void{
        _labelWidth = w;
        _input.x = _labelWidth +10;
        _input.width = super.width - 15 - _labelWidth;
    }

    public function get labelWidth():int {
        return _labelWidth;
    }


    public override function get label():String {
        return _label.text;
    }

    public override function set label(val:String):void {
        _label.text = val;
        if(Util.isEmpty(_localpath)){
            _localpath = label.toLowerCase();
        }
    }

    public function get inputComponent():UIComponent {
        if(_input==null){
           createInput();
        }

        return _input;
    }

    public function get labelComponent():UIComponent {
        return _label;
    }


    public function get localpath():String {
        return _localpath;
    }

    public function get path():String {
        if(Util.isEmpty(_fullpath)){
            _fullpath = editPanel.path +"/"+ _localpath
        }
        return _fullpath;
    }

    public function getValue():Object {
       return TextInput(_input).text;
    }

    public function setValue(val:Object):void {
       TextInput(_input).text = val.toString();
    }

    public function get editPanel():EditPanel {
        return EditPanel(parent);
    }

}
}