import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;

import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

class CurrentTimeReplacer {
    private static final Logger logger = LogManager.getLogger();
    private Config configObject;

    CurrentTimeReplacer(String configFilePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        configObject = mapper.readValue(new File(configFilePath), Config.class);
    }

    // TODO: Can URISyntaxException even happen?
    private String getCurrentTime() throws IOException, URISyntaxException {
        HttpResponse<JsonNode> response = Unirest.get(configObject.timeApi.url)
                .queryString("accesskey", configObject.timeApi.timeApiAccessKey)
                .queryString("secretkey", configObject.timeApi.timeApiSecretKey)
                .queryString("placeid", configObject.timeApi.placeId)
                .asJson();

        return response.getBody().getObject().getJSONArray("locations").
                getJSONObject(0).getJSONObject("time").get("iso").toString();
    }

    void updateCurrentTime() throws IOException, URISyntaxException {
        File fileToUpdate = new File(configObject.fileToUpdate);
        File tempFile = File.createTempFile("java-ex-time-replaced-", null);

        BufferedReader reader = null;
        BufferedWriter writer = null;
        // TODO: Is the ISO format ok?
        String currentTime = getCurrentTime();
        try {
            reader = new BufferedReader(new FileReader(fileToUpdate));
            writer = new BufferedWriter(new FileWriter(tempFile));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                writer.write(line.replaceAll(configObject.textToUpdate, currentTime));
                writer.newLine();
            }
        } catch (IOException e) {
            tempFile.delete();
            throw e;
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
        Files.move(tempFile.toPath(), fileToUpdate.toPath(), REPLACE_EXISTING);
    }
}

class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
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