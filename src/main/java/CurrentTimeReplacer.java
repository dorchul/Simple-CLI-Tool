import java.io.*;
/*import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;*/

import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.apache.log4j.Logger;

class CurrentTimeReplacer {
  private final Config config;
  private static final Logger logger = Logger.getLogger(CurrentTimeReplacer.class);
  private static final String TEMP_FILE_PATH = "tempFile.txt";

  CurrentTimeReplacer(String configFilePath) throws IOException {
    // mapper from json to and from java objects
    ObjectMapper mapper = new ObjectMapper();

    // map config file to config object
    this.config = mapper.readValue(new File(configFilePath), Config.class);
    logger.info("created config object: " + config.toString());
  }

  private String getCurrentTime() {
    // get request to the time service api
    HttpResponse<JsonNode> response =
        Unirest.get(config.timeApi.url)
            .queryString("accesskey", config.timeApi.timeApiAccessKey)
            .queryString("secretkey", config.timeApi.timeApiSecretKey)
            .queryString("placeid", config.timeApi.placeId)
            .asJson();

    logger.info("finished time service api request");

    // extract the time in iso format
    String isoTime =
        response
            .getBody()
            .getObject()
            .getJSONArray("locations")
            .getJSONObject(0)
            .getJSONObject("time")
            .get("iso")
            .toString();

    logger.info("Extracted time in iso format: " + isoTime);

    return isoTime;
  }

  void updateCurrentTime() throws IOException {
    File fileToUpdate = new File(config.fileToUpdate);
    File newFile = new File(TEMP_FILE_PATH);
    String currentTime = getCurrentTime();

    // create reader for the old file and writer for the new file.
    try (BufferedReader reader = new BufferedReader(new FileReader(fileToUpdate));
        BufferedWriter writer = new BufferedWriter(new FileWriter(newFile))) {

      logger.info("created reader and writer");

      // read from the old file and write to the new file, line by line.
      for (String line = reader.readLine(); line != null; line = reader.readLine()) {
        writer.write(line.replaceAll(config.textToUpdate, currentTime));
        writer.newLine();
      }

    } catch (IOException e) {
      if (!newFile.delete()) {
        logger.error("failed to delete the new file after IO exception", e);
      }
      logger.error("failed to create reader and writer", e);
      throw e;
    }

    // replace current file with the updated file
    if (!fileToUpdate.delete()) {
      logger.info("failed to delete the old file");
    }
    if (!newFile.renameTo(fileToUpdate)) {
      logger.info("failed to rename the old file");
    }
  }
}
