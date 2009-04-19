package de.ama.framework.data {
public class SelectionModel {
    private var _selections:Array;
    private var _condition:String;
    private var _type:String;


    public function get selections():Array {
        return _selections;
    }

    public function set selections(val:Array):void {
        _selections = val;
    }

    public function get condition():String {
        return _condition;
    }

    public function set condition(val:String):void {
        _condition = val;
    }

    public function get type():String {
        return _type;
    }

    public function set type(val:String):void {
        _type = val;
    }
}
}