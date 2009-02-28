package components
{
    import flash.errors.IllegalOperationError;
    
    public class StringTokenizer 
    {
        protected var source:String;
        protected var delimiter:String;
        protected var tokens:Array;
        protected var cursor:int;

        public function StringTokenizer(source:String, delimiter:String)
        {
            this.source = source;
            this.delimiter = delimiter;

            this.tokens = source.split( delimiter );
        }

        public function size() : int
        {
            return tokens.length;
        }

        public function hasMoreTokens() : Boolean
        {
            return cursor < tokens.length;
        }

        public function nextToken() : String
        {
            var token:String;

            if ( hasMoreTokens() )
            {
                token = tokens[cursor];
                cursor++;
            }

            return token;
        }

        public function hasNext() : Boolean
        {
            return hasMoreTokens();
        }

        public function next() : *
        {
            return nextToken();
        }


        public function hasMoreElements() : Boolean
        {
            return hasMoreTokens();
        }

        public function nextElement() : *
        {
            return nextToken();
        }
    }
}