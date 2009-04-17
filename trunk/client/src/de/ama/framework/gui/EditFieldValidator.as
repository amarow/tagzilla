package de.ama.framework.gui
{
import mx.validators.StringValidator;
import mx.validators.ValidationResult;

public class EditFieldValidator extends StringValidator
	{
		public var forbiddenChars:String = "";
		private var results:Array;
		      
		public function EditFieldValidator()
		{
			super();
		}
		
        override protected function doValidation(value:Object):Array {
        
            var inputValue:String = String(value);
            results = [];
            results = super.doValidation(value);        
            if (results.length > 0)
                return results;
        
            var i:int = 0;
            var tmp:String;
            for(i=0;i<forbiddenChars.length;i++){
              	tmp=forbiddenChars.charAt(i);
              	if(inputValue.indexOf(tmp)>=0){
                results.push(new ValidationResult(true, null, "forbidden char", 
                    "["+tmp+"] is a forbidden character"));
              	}
            }
            
            return results;
        }
	}
}