import java.util.*;
import java.io.*;

public class Server {

  public static void main(String args[]) {
    TimerTask task = new DirWatcher("c:/temp", "xml" ) {
      protected void onChange( File file, String action ) {
        // here we code the action on a change
        System.out.println
           ( "File "+ file.getName() +" action: " + action );
      }
    };

    Timer timer = new Timer();
    timer.schedule( task , new Date(), 5000 );
  }
}