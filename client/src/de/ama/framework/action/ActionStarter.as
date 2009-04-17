package de.ama.framework.action {
import de.ama.framework.util.Environment;

import de.ama.framework.util.Util;

import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.mxml.RemoteObject;

public class ActionStarter {

    private static var _instance:ActionStarter = null;

    private  var stub:RemoteObject = new RemoteObject();
    private  var startedAction:ActionScriptAction;
    private  var _invoker:Object;


    public static function get instance():ActionStarter {
    	if(_instance==null){
    	   _instance= new ActionStarter();	
    	}
        return _instance;
    }

    public function ActionStarter() {
        stub.endpoint = Environment.getServerUrl() + "/messagebroker/amf";
        stub.destination = "ActionService";
        stub.addEventListener("result", actionResultHandler);
        stub.addEventListener("fault", actionFaultHandler);
        stub.showBusyCursor = true;
        stub.concurrency = "single";
    }


    public function execute(action:ActionScriptAction, invoker:Object = null):void {
        _invoker = invoker;
        this.startedAction = action;
        action.message=null;
        action.detailErrorMessage=null;
        action.userId=Environment.userId;

        action.onBeforeCall(this);
        stub.execute(action);
    }


    public function actionResultHandler(event:ResultEvent):void {
        var action:ActionScriptAction = event.result as ActionScriptAction;
        if (action.detailErrorMessage != null) {
            Util.showError(action.detailErrorMessage);
        }

        
        action.onAfterCall(this);
    }

    public function actionFaultHandler(event:FaultEvent):void {
        Util.showError(event.fault.faultString)
    }


    public function get invoker():Object {
        return _invoker;
    }}
}