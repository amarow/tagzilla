package components
{
	public class AsData {	

	public var oidString:String;
	public var version:int;
		
		public function saveToString(o:Object,def:String=""):String {
		    if(o==null)return def;
		    return o.toString();
		}
	}
	
}