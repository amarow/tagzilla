package de.ama.framework.util {
public class Callback {
    private var _obj:Object;
    private var _function:Function;


    public function Callback(obj:Object, func:Function){
        this._function = func;
        this._obj = obj;
    }


    public function execute(args:*):*{
        return _function.call(_obj,args);
    }
    
}
}