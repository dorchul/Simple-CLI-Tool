FROM openjdk:11
ADD /target/discount_ex-1.0-SNAPSHOT-jar-with-dependencies.jar discount_ex-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","discount_ex-1.0-SNAPSHOT.jar"]