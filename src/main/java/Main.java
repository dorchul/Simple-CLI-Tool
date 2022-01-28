import org.apache.log4j.LogManager;

import java.io.IOException;
import java.net.URISyntaxException;

class Main
{
    public static void main(String[] args)
    {
/*        System.out.println();
        if (args.length != 1)
        {
            System.err.println("Usage: [program] [config_file_path]");
            System.exit(1);
        }*/

        String configFilePath;
        if (args.length == 0)
        {
            configFilePath = "/home/config.json";
        }

        else
        {
            configFilePath = args[0];
        }

        try
        {
            CurrentTimeReplacer replacer = new CurrentTimeReplacer(configFilePath);
            replacer.updateCurrentTime();
        }

        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
