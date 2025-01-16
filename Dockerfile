# Build a JAR File
FROM maven:3.8.3-openjdk-17-slim AS stage1
WORKDIR /home/app
COPY . /home/app/
RUN mvn -f /home/app/pom.xml clean package
RUN ls -l target
# Create an Image
FROM openjdk:17-jdk-alpine
EXPOSE 8080
COPY --from=stage1 /home/app/target/huSpark-0.0.1-SNAPSHOT.jar hello-world-java.jar
ENTRYPOINT ["sh", "-c", "java -jar /hello-world-java.jar"]
