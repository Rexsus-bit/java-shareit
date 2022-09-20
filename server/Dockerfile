FROM amazoncorretto:11-alpine-jdk
COPY target/*exec.jar server.jar
ENTRYPOINT ["java","-jar","/server.jar"]