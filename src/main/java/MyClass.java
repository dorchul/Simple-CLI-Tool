import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;

import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MyClass {
    private static final Logger logger = LogManager.getLogger();
    private static Config configObject;

    public static void main(String[] args) throws IOException, URISyntaxException {
        if (args.length != 1) {
            System.err.println("Usage: [program] [config_file_path]");
            System.exit(1);
        }
        ObjectMapper mapper = new ObjectMapper();
        MyClass.configObject = mapper.readValue(new File(args[0]), Config.class);
        updateCurrentTime();
    }

    private static String getCurrentTime() throws IOException, URISyntaxException {
        HttpResponse<JsonNode> response = Unirest.get(configObject.timeApi.url)
                .queryString("accesskey", configObject.timeApi.timeApiAccessKey)
                .queryString("secretkey", configObject.timeApi.timeApiSecretKey)
                .queryString("placeid", configObject.timeApi.placeId)
                .asJson();

        return response.getBody().getObject().getJSONArray("locations").
                getJSONObject(0).getJSONObject("time").get("iso").toString();
    }

    public static void updateCurrentTime() throws IOException, URISyntaxException {
        String textToUpdate = configObject.textToUpdate;
        File fileToUpdate = new File(configObject.fileToUpdate);
        File tempFile = File.createTempFile("java-ex-time-replaced-", null);

        BufferedReader reader = new BufferedReader(new FileReader(fileToUpdate));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        // TODO: Is the ISO format ok?
        // TODO: Handle errors and make sure the files are closed.
        String currentTime = getCurrentTime();
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            writer.write(line.replaceAll(textToUpdate, currentTime));
            writer.newLine();
        }

        tempFile.renameTo(fileToUpdate);
        reader.close();
        writer.close();
    }
}
