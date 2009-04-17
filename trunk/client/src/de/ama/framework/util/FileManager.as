package de.ama.framework.util {
import flash.events.Event;
import flash.events.IOErrorEvent;
import flash.net.FileReference;
import flash.net.URLRequest;
import flash.net.URLVariables;
import flash.net.navigateToURL;

import de.ama.framework.util.*;

public class FileManager {


    public function showFile(path:String):void {
        var request:URLRequest = new URLRequest(Environment.getServerUrl()+"/download");
        var variables:URLVariables = new URLVariables();
        variables.path = path;
        request.method = "POST";
        request.data = variables;

        
        try {
            navigateToURL(request,"Tagzilla-Preview");
        } catch (e:Error) {
            Util.showError(e.message);
        }
    }



    ///////////////////////////////// download File ////////////////////////////////////////

    







    ///////////////////////////////// upload File ////////////////////////////////////////

    public function saveFile(filedata:Object, fname:String):void {
        var fr:FileReference = new FileReference();
        fr.addEventListener(Event.COMPLETE, onFileSave);
        fr.addEventListener(Event.CANCEL, onCancel);
        fr.addEventListener(IOErrorEvent.IO_ERROR, onSaveError);
        fr.save(filedata , fname);
    }

    private function onFileSave(e:Event):void {
    }

    private function onCancel(e:Event):void {
    }

    private function onSaveError(e:IOErrorEvent):void {
        Util.showError("Error Saving File : " + e.text);
    }


}
}