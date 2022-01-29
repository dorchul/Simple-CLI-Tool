FROM maven:3.8.4-openjdk-17

WORKDIR /app
COPY . .
RUN mvn install

ENTRYPOINT ["mvn", "exec:java", "-Dexec.args=run/config.json"] 
