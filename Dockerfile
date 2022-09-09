FROM openjdk:8-jdk-alpine
COPY ./target/tengen-era.jar /root/startup/
WORKDIR /root/startup/
EXPOSE 8080
ENTRYPOINT ["java","-jar","/tengen-era.jar"]