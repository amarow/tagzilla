package de.ama.framework.util {
import flash.events.Event;

public class BroadcastEvent extends Event{

    public static const BROADCAST:String = "broadcast";

    public var data:Object;

    public function BroadcastEvent(type:String, data:Object) {
        super(type,true,false);
        this.data=data;
    }


    override public function clone():Event {
        return new BroadcastEvent(type,data);
    }

}
}