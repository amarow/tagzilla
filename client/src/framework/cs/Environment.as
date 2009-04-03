package framework.cs {
public class Environment {

    private static var _hostAdress:String;
    private static var _hostPort:String;
    private static var _hostContext:String;


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




}
}