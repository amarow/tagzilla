package bom {
[RemoteClass(alias="de.ama.server.bom.CrawlerData")]
	public class CrawlerData
	{
		public var rootPath:String;
        public var pause:Number;
        public var running:Boolean;
        public var scannedFilesCount:Number;

         
		public function CrawlerData(path:String=""){
			rootPath=path;
			pause=5000;
		}
		
        
	}
}