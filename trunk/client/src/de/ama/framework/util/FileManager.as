package de.ama.framework.util {
import de.ama.framework.action.ActionStarter;
import de.ama.framework.action.FileAction;

import flash.events.Event;
import flash.events.IOErrorEvent;
import flash.events.ProgressEvent;
import flash.net.FileReference;
import flash.net.URLRequest;
import flash.net.URLVariables;
import flash.net.navigateToURL;

public class FileManager {

    ///////////////////////////////// navigate to File ////////////////////////////////////////

    public function showFile(path:String):void {
        var request:URLRequest = new URLRequest(Environment.getServerUrl() + "/download");
        var variables:URLVariables = new URLVariables();
        variables.path = path;
        request.method = "POST";
        request.data = variables;


        try {
            navigateToURL(request, "Tagzilla-Preview");
        } catch (e:Error) {
            Util.showError(e.message);
        }
    }

    ///////////////////////////////// upload File ////////////////////////////////////////


    private var serverPath:String;
    var fileRef:FileReference = new FileReference();

    public function uploadFile(serverPath:String):void {
        fileRef.addEventListener(Event.SELECT, fileRef_select);
        fileRef.addEventListener(Event.COMPLETE, fileRef_loadComplete);
        this.serverPath = serverPath;
        fileRef.browse();
    }

    private function fileRef_select(e:Event):void {
        fileRef.load();
    }

    private function fileRef_loadComplete(e:Event):void {
        var fa:FileAction = new FileAction();
        fa.fileName = serverPath;
        fa.data = fileRef.data;
        ActionStarter.instance.execute(fa) ;
    }

    //    private function fileRef_select(e:Event):void {
    //        try {
    //            var fileRef:FileReference = FileReference(e.target);
    //            var request:URLRequest = new URLRequest(Environment.getServerUrl() + "/upload");
    //            var variables:URLVariables = new URLVariables();
    //            variables.fileName = fileRef.name;
    //            variables.serverPath = this.serverPath;
    //
    //            request.method = "POST";
    //            request.data = variables;
    //            fileRef.upload(request);
    //        } catch (err:Error) {
    //            Util.showError("File upload failed");
    //        }
    //
    //    }
    //


    ///////////////////////////////// save data to File ////////////////////////////////////////

    public function saveFile(filedata:Object, fname:String):void {
        var fr:FileReference = new FileReference();
        fr.addEventListener(Event.COMPLETE, onFileSave);
        fr.addEventListener(Event.CANCEL, onCancel);
        fr.addEventListener(IOErrorEvent.IO_ERROR, onSaveError);
        fr.save(filedata, fname);
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