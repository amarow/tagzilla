package framework.cs {
public class Environment {

    private static var _hostAdress:String;
    private static var _hostPort:String;
    private static var _hostContext:String;
    private static var _user:String;
    private static var _pwd:String;
    private static var _userId:Number;


    public static function initForProduction():void{
        _hostAdress     = "localhost";
        _hostPort       = "8400";
        _hostContext    = "tagzilla";
    }


    public static function getServerUrl():String {
        return "http://"+_hostAdress+":"+_hostPort+"/"+_hostContext;
    }

    public static function get hostContext():String {
        return _hostContext;
    }

    public static function get hostAdress():String {
        return hostAdress;
    }

    public static function get hostPort():String {
        return hostPort;
    }


    public static function get user():String {
        return _user;
    }

    public static function get pwd():String {
        return _pwd;
    }

    public static function get userId():Number {
        return _userId;
    }


    public static function registerLoginData(la:LoginAction):void{
       _user=la._user;
       _pwd=la._pwd;
       _userId=la.userId;
    }

    public static function eraseLoginData(la:LogoutAction):void{
        if(la.success){
            _user="";
            _pwd="";
            _userId=-1;
        }
    }

}
}