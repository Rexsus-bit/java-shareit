FROM amazoncorretto:11-alpine-jdk
COPY target/*exec.jar shareit-server.jar
ENTRYPOINT ["java","-jar","/shareit-server.jar"]