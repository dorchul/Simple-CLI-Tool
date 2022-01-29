class TimeApiConfig {
    public String url;
    public String timeApiAccessKey;
    public String timeApiSecretKey;
    public String placeId;

    @Override
    public String toString() {
        return "TimeApiConfig{" +
                "url='" + url + '\'' +
                ", timeApiAccessKey='" + timeApiAccessKey + '\'' +
                ", timeApiSecretKey='" + timeApiSecretKey + '\'' +
                ", placeId='" + placeId + '\'' +
                '}';
    }
}

class Config {
    public TimeApiConfig timeApi;
    public String textToUpdate;
    public String fileToUpdate;

    @Override
    public String toString() {
        return "Config{" +
                "timeApi=" + timeApi +
                ", textToUpdate='" + textToUpdate + '\'' +
                ", fileToUpdate='" + fileToUpdate + '\'' +
                '}';
    }
}