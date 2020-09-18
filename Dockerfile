FROM navikt/java:14
COPY target/app.jar /app/app.jar
EXPOSE 8080
