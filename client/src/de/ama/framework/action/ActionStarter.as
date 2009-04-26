package de.ama.framework.action {
import de.ama.framework.util.Environment;

import de.ama.framework.util.Util;
import hessian.client.HessianService;

import mx.rpc.AsyncToken;
import mx.rpc.IResponder;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.mxml.RemoteObject;

public class ActionStarter implements IResponder{

    private static var _instance:ActionStarter = null;

    private  var blazedsStub:RemoteObject = null;
    private  var hessianStub:HessianService = null;

    private  var startedAction:ActionScriptAction;
    private  var _invoker:Object;


    public static function get instance():ActionStarter {
        if (_instance == null) {
            _instance = new ActionStarter();
        }
        return _instance;
    }

    public function ActionStarter() {

        if (Environment.useHessianProtocoll()) {
            hessianStub = new HessianService(Environment.getServerUrl() + "/action");
//            hessianStub.showBusyCursor = true;
//            hessianStub.concurrency = "single";
        } else {
            blazedsStub = new RemoteObject();
            blazedsStub.endpoint = Environment.getServerUrl() + "/messagebroker/amf";
            blazedsStub.destination = "ActionService";
            blazedsStub.addEventListener("result", actionResultHandler);
            blazedsStub.addEventListener("fault",  actionFaultHandler);
            blazedsStub.showBusyCursor = true;
            blazedsStub.concurrency = "single";
        }
    }


    public function execute(action:ActionScriptAction, invoker:Object = null):void {
        _invoker = invoker;
        this.startedAction = action;
        action.message = null;
        action.detailErrorMessage = null;
        action.userId = Environment.userId;
        action.onBeforeCall(this);

        if (hessianStub != null) {
            var token:AsyncToken = hessianStub.execute.send(action);
            token.addResponder(this);
        } else {
            blazedsStub.execute(action);
        }
    }


    public function actionResultHandler(event:ResultEvent):void {
        var action:ActionScriptAction = event.result as ActionScriptAction;
        if (action.detailErrorMessage != null) {
            Util.showError(action.detailErrorMessage);
        }

        if(_invoker is Function){
            Function(_invoker)(action);
        } else {
            action.onAfterCall(this);
        }
    }

    public function actionFaultHandler(event:FaultEvent):void {
       Util.showError(event.fault.faultString)
    }


    public function get invoker():Object {
        return _invoker;
    }


    public function result(data:Object):void {
        actionResultHandler(ResultEvent(data))
    }

    public function fault(info:Object):void {
//        if (info is FaultEvent) {
//            actionFaultHandler(FaultEvent(info))
//        }
    }
}
}