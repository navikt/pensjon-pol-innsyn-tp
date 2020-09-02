FROM navikt/java:13
COPY target/app.jar /app/app.jar
EXPOSE 8080
