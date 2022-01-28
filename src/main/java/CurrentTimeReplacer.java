import java.io.*;
import java.nio.file.Files;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.apache.log4j.Logger;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

class CurrentTimeReplacer
{
    private final Config config;
/*    private final Logger logger;*/

    CurrentTimeReplacer(String configFilePath) throws IOException
    {
/*        this.logger = logger;*/

        // mapper from jason to/from java objects
        ObjectMapper mapper = new ObjectMapper();

        // map config file to config object
        this.config = mapper.readValue(new File(configFilePath), Config.class);

/*        this.logger.info("initialized config successfully");*/
    }

    private String getCurrentTime()
    {
        // get request to the time service api to get the current time at the specified location
        HttpResponse<JsonNode> response = Unirest.get(config.timeApi.url)
                .queryString("accesskey", config.timeApi.timeApiAccessKey)
                .queryString("secretkey", config.timeApi.timeApiSecretKey)
                .queryString("placeid", config.timeApi.placeId)
                .asJson();

        // return only the relevant field (time in iso format) as a string
        return response.getBody().getObject().getJSONArray("locations").
                getJSONObject(0).getJSONObject("time").get("iso").toString();
    }

    void updateCurrentTime() throws IOException
    {
        File fileToUpdate;
        if (config.fileToUpdate == null) {
            fileToUpdate = new File("/home/input.txt");
        } else {
            fileToUpdate = new File(config.fileToUpdate);
        }

        // create a temporary file to buffer the updated text
        File tempFile = File.createTempFile("java-ex-time-replaced-", null);

        BufferedReader reader = null;
        BufferedWriter writer = null;

        // get the current time
        String currentTime = getCurrentTime();

        try
        {
            // reader for the old content
            reader = new BufferedReader(new FileReader(fileToUpdate));

            // writer for the updated content
            writer = new BufferedWriter(new FileWriter(tempFile));
/*

            logger.info("created a buffered reader and writer successfully");
*/

            // iterate through file lines
            // for each line, write the updated line to the file for the updated content
            for (String line = reader.readLine(); line != null; line = reader.readLine())
            {
                writer.write(line.replaceAll(config.textToUpdate, currentTime));
                writer.newLine();
            }
        }

        catch (IOException e)
        {
            tempFile.delete();
/*            logger.error("failed file I/O operation", e);*/
            throw e;
        }

        finally
        {
            if (reader != null)
            {
                reader.close();
            }

            if (writer != null)
            {
                writer.close();
            }
        }

        // replace current file with the updated file
        Files.move(tempFile.toPath(), fileToUpdate.toPath(), REPLACE_EXISTING);
    }
}
