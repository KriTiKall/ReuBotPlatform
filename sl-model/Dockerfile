FROM openjdk:8
COPY ./target/sl-model-*-jar-with-dependencies.jar /tmp/model.jar
WORKDIR /tmp
ENTRYPOINT ["java", "-jar","model.jar"]

EXPOSE 9090