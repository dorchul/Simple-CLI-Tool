import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.io.IOException;

class Main {

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("Usage: [program] [config_file_path]");
      System.exit(1);
    }

    try {
      CurrentTimeReplacer replacer = new CurrentTimeReplacer(args[0]);
      replacer.updateCurrentTime();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
}
