package bom {
import actions.*;
	[RemoteClass(alias="de.ama.server.bom.Crawler")]
	public class Crawler
	{
		public var rootPath:String;
        public var pause:Number;
        public var running:Boolean;
        public var scannedFilesCount:Number;

         
		public function Crawler(path:String=""){
			rootPath=path;
			pause=5000;
		}
		
        
	}
}