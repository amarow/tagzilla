package de.ama.framework.gui {
import de.ama.framework.util.Util;

import mx.controls.CheckBox;

public class BoolField extends EditField{


    public function BoolField(caption:String="BoolField", path:String=null) {
        super(caption,path);
    }

    public override function createInput():void{
        _input = new CheckBox();
        _input.x = labelWidth+10;
        addChild(_input);
    }

    public override function setValue(val:Object):void {
        var sel:Boolean;
        if(val is Boolean) sel = Boolean(val);
        if(val is String)  sel = Util.isEqual(val,"true", true);
        if(val is Number)  sel = Util.isEqual(val,1);

        CheckBox(_input).selected = sel;
    }


    override public function getValue():Object {
        return CheckBox(_input).selected;
    }
}
}